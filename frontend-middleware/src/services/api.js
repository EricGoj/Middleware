import axios from 'axios'
import { isMockMode, mockApiService } from '../mocks/index.js'

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080'

const apiClient = axios.create({
  baseURL: API_BASE_URL,
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json',
  },
})

// Request interceptor for auth token if needed
apiClient.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('authToken')
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  (error) => {
    console.error('Request error:', error)
    return Promise.reject(error)
  }
)

// Response interceptor for error handling
apiClient.interceptors.response.use(
  (response) => response,
  (error) => {
    console.error('Response error:', error)
    if (error.response?.status === 401) {
      localStorage.removeItem('authToken')
      window.location.href = '/login'
    }
    return Promise.reject(error)
  }
)

// Helper function to transform backend data to frontend format
const transformTaskFromBackend = (backendTask) => ({
  id: backendTask.id,
  titulo: backendTask.title,
  descripcion: backendTask.description,
  fecha: backendTask.dueDate || backendTask.createdAt,
  estado: backendTask.status,
  completada: backendTask.status === 'DONE'
})

// Helper function to transform frontend data to backend format
const transformTaskToBackend = (frontendTask) => ({
  title: frontendTask.titulo,
  description: frontendTask.descripcion,
  dueDate: frontendTask.fecha,
  status: frontendTask.estado,
  priority: 'Medium' // Default priority
})

// API service principal con soporte para mocks
const createApiService = () => {
  // Si estamos en modo mock, usar mock service
  if (isMockMode()) {
    console.log('🎭 Usando mock API service')
    return {
      getIntegration: () => mockApiService.getIntegrationStatus(),
      getTasks: (params) => mockApiService.getTasks(params),
      createTask: (taskData) => mockApiService.createTask(taskData),
      updateTask: (id, taskData) => mockApiService.updateTask(id, taskData),
      deleteTask: (id) => mockApiService.deleteTask(id),
    }
  }

  // API real para producción
  return {
  // Integration endpoint
  getIntegration: () => apiClient.get('/api/integration'),
  
  // Task CRUD operations with data transformation
  getTasks: async () => {
    const response = await apiClient.get('/api/issues')
    return {
      ...response,
      data: response.data.map(transformTaskFromBackend)
    }
  },
  
  createTask: async (taskData) => {
    const backendData = transformTaskToBackend(taskData)
    const response = await apiClient.post('/api/issues', backendData)
    return {
      ...response,
      data: transformTaskFromBackend(response.data)
    }
  },
  
  updateTask: async (id, taskData) => {
    const backendData = transformTaskToBackend(taskData)
    const response = await apiClient.patch(`/api/issues/${id}`, backendData)
    return {
      ...response,
      data: transformTaskFromBackend(response.data)
    }
  },
  
    deleteTask: (id) => apiClient.delete(`/api/issues/${id}`),
  }
}

export const api = createApiService()