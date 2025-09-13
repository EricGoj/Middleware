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

// Note: Using backend field names directly to avoid unnecessary transformations
// Backend fields: id, title, description, status, createdAt, updatedAt, dueDate, priority

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
    return response.data // Return data directly without transformation
    const response = await apiClient.get('/api/issues')
    return {
      ...response,
      data: response.data.map(transformTaskFromBackend)
    }
  },
  
  createTask: async (taskData) => {
    // Ensure default priority if not provided
    const dataWithDefaults = {
      priority: 'MEDIUM',
      ...taskData
    }
    const response = await apiClient.post('/api/issues', dataWithDefaults)
    return response.data // Return data directly without transformation
    const backendData = transformTaskToBackend(taskData)
    const response = await apiClient.post('/api/issues', backendData)
    return {
      ...response,
      data: transformTaskFromBackend(response.data)
    }
  },
  
  updateTask: async (id, taskData) => {
    const response = await apiClient.patch(`/api/issues/${id}`, taskData)
    return response.data // Return data directly without transformation
    const backendData = transformTaskToBackend(taskData)
    const response = await apiClient.patch(`/api/issues/${id}`, backendData)
    return {
      ...response,
      data: transformTaskFromBackend(response.data)
    }
  },
  
    deleteTask: (id) => apiClient.delete(`/api/issues/${id}`),
    deleteTask: (id) => apiClient.delete(`/api/issues/${id}`),
  }
}

export const api = createApiService()