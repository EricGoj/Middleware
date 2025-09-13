// Mock system para desarrollo sin backend
import { mockApiService, mockWebSocketEvents } from './apiService.js'

// Detectar si estamos en modo mock
export const isMockMode = () => {
  return false;
}

// Inicializar sistema de mocks
export const initializeMocks = () => {
  if (isMockMode()) {
    console.log('🎭 Mock mode activado - usando datos de prueba')
    
    // Configurar interceptores globales para development
    if (typeof window !== 'undefined') {
      window.__MOCK_API__ = mockApiService
      window.__MOCK_WS__ = mockWebSocketEvents
    }
    
    return true
  }
  
  return false
}

// Exportar servicios mock
export { mockApiService, mockWebSocketEvents }
export { mockTasks, mockIntegration, generateMockTask, getRandomMockTask } from './data.js'