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
    return Promise.reject(error)
  }
)

// Response interceptor for error handling
apiClient.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      localStorage.removeItem('authToken')
      window.location.href = '/login'
    }
    return Promise.reject(error)
  }
)

const issueApi = {
  // Get all issues
  getIssues: async () => {
    if (isMockMode()) {
      return mockApiService.getIssues()
    }
    const response = await apiClient.get('/api/issues')
    return response.data
  },

  // Get issue by ID
  getIssue: async (id) => {
    if (isMockMode()) {
      return mockApiService.getIssue(id)
    }
    const response = await apiClient.get(`/api/issues/${id}`)
    return response.data
  },

  // Create new issue
  createIssue: async (issueData) => {
    if (isMockMode()) {
      return mockApiService.createIssue(issueData)
    }
    const response = await apiClient.post('/api/issues', issueData)
    return response.data
  },

  // Update issue
  updateIssue: async (id, issueData) => {
    if (isMockMode()) {
      return mockApiService.updateIssue(id, issueData)
    }
    const response = await apiClient.patch(`/api/issues/${id}`, issueData)
    return response.data
  },

  // Delete issue
  deleteIssue: async (id) => {
    if (isMockMode()) {
      return mockApiService.deleteIssue(id)
    }
    await apiClient.delete(`/api/issues/${id}`)
  },

  // Sync with Jira
  syncWithJira: async (issueId) => {
    if (isMockMode()) {
      return mockApiService.syncWithJira(issueId)
    }
    const response = await apiClient.post(`/api/jira/sync/${issueId}`)
    return response.data
  }
}

export { issueApi as api }