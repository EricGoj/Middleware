// Mock API service para desarrollo sin backend
import { mockTasks, mockIntegration, generateMockTask } from './data.js'

// Simular delay de red para testing realista
const simulateNetworkDelay = (ms = 300) => 
  new Promise(resolve => setTimeout(resolve, ms))

// Mock de respuestas del servidor
class MockApiService {
  constructor() {
    // Clonar datos mock para manipulación
    this.tasks = [...mockTasks]
    this.integration = { ...mockIntegration }
  }

  // GET /api/tasks
  async getTasks(params = {}) {
    await simulateNetworkDelay()
    
    let filteredTasks = [...this.tasks]
    
    // Simular filtros del backend
    if (params.estado) {
      filteredTasks = filteredTasks.filter(task => task.estado === params.estado)
    }
    
    if (params.search) {
      const searchLower = params.search.toLowerCase()
      filteredTasks = filteredTasks.filter(task => 
        task.titulo.toLowerCase().includes(searchLower) ||
        task.descripcion.toLowerCase().includes(searchLower)
      )
    }
    
    return {
      data: filteredTasks,
      status: 200
    }
  }

  // POST /api/tasks
  async createTask(taskData) {
    await simulateNetworkDelay()
    
    const newTask = generateMockTask({
      ...taskData,
      id: `mock-${Date.now()}`
    })
    
    this.tasks.push(newTask)
    
    return {
      data: newTask,
      status: 201
    }
  }

  // PUT /api/tasks/:id
  async updateTask(id, taskData) {
    await simulateNetworkDelay()
    
    const taskIndex = this.tasks.findIndex(task => task.id === id)
    
    if (taskIndex === -1) {
      throw new Error(`Tarea con ID ${id} no encontrada`)
    }
    
    const updatedTask = {
      ...this.tasks[taskIndex],
      ...taskData,
      fechaActualizacion: new Date().toISOString()
    }
    
    this.tasks[taskIndex] = updatedTask
    
    return {
      data: updatedTask,
      status: 200
    }
  }

  // DELETE /api/tasks/:id
  async deleteTask(id) {
    await simulateNetworkDelay()
    
    const taskIndex = this.tasks.findIndex(task => task.id === id)
    
    if (taskIndex === -1) {
      throw new Error(`Tarea con ID ${id} no encontrada`)
    }
    
    this.tasks.splice(taskIndex, 1)
    
    return {
      status: 204
    }
  }

  // GET /api/integration/status
  async getIntegrationStatus() {
    await simulateNetworkDelay()
    
    return {
      data: this.integration,
      status: 200
    }
  }

  // Simular errores para testing
  async simulateError(type = 'network') {
    await simulateNetworkDelay(1000)
    
    const errors = {
      network: new Error('Error de conexión de red'),
      server: new Error('Error interno del servidor (500)'),
      unauthorized: new Error('No autorizado (401)'),
      notFound: new Error('Recurso no encontrado (404)')
    }
    
    throw errors[type] || errors.network
  }

  // Resetear datos mock al estado inicial
  reset() {
    this.tasks = [...mockTasks]
    this.integration = { ...mockIntegration }
  }
}

// Exportar instancia singleton
export const mockApiService = new MockApiService()

// Mock WebSocket events para testing
export const mockWebSocketEvents = {
  // Simular evento de nueva tarea desde Jira
  simulateTaskCreated: (dispatch) => {
    const newTask = generateMockTask({
      titulo: 'Tarea desde Jira Mock',
      descripcion: 'Tarea creada automáticamente desde integración Jira simulada'
    })
    
    dispatch({
      type: 'tasks/upsertFromWs',
      payload: newTask
    })
  },

  // Simular actualización de tarea
  simulateTaskUpdated: (dispatch, taskId, updates) => {
    dispatch({
      type: 'tasks/upsertFromWs',
      payload: { id: taskId, ...updates }
    })
  },

  // Simular eliminación de tarea
  simulateTaskDeleted: (dispatch, taskId) => {
    dispatch({
      type: 'tasks/removeFromWs',
      payload: taskId
    })
  }
}