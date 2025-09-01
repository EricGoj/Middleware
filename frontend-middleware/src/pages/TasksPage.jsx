import { useEffect, useState } from 'react'
import { useSelector, useDispatch } from 'react-redux'
import { Link } from 'react-router-dom'
import { fetchTasks } from '../features/tasks/tasksSlice.js'
import { fetchIntegration } from '../features/integration/integrationSlice.js'
import { connectWebSocket, disconnectWebSocket } from '../services/ws.js'
import Table from '../components/Table.jsx'
import { ToastContainer } from '../components/Toast.jsx'

/**
 * Main tasks page component
 */
const TasksPage = () => {
  const dispatch = useDispatch()
  const { loading: tasksLoading, error: tasksError } = useSelector(state => state.tasks)
  const { provider, loading: integrationLoading, error: integrationError } = useSelector(state => state.integration)
  const [toasts, setToasts] = useState([])

  useEffect(() => {
    // Load initial data
    dispatch(fetchTasks())
    dispatch(fetchIntegration())
    
    // Connect to WebSocket
    const ws = connectWebSocket(dispatch)
    
    // Cleanup on unmount
    return () => {
      disconnectWebSocket()
    }
  }, [dispatch])

  const showToast = (message, type = 'info', duration = 5000) => {
    const id = Date.now() + Math.random()
    const newToast = { id, message, type, duration }
    setToasts(prev => [...prev, newToast])
  }

  const removeToast = (id) => {
    setToasts(prev => prev.filter(toast => toast.id !== id))
  }

  const handleRefresh = () => {
    dispatch(fetchTasks())
    showToast('Tareas actualizadas', 'success')
  }

  // Show errors as toasts
  useEffect(() => {
    if (tasksError) {
      showToast(tasksError, 'error')
    }
  }, [tasksError])

  useEffect(() => {
    if (integrationError) {
      showToast(integrationError, 'error')
    }
  }, [integrationError])

  return (
    <main className="min-h-screen bg-gray-100">
      <div className="container mx-auto px-4 py-8">
        {/* Header */}
        <div className="mb-8">
          <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between">
            <div>
              <h1 className="text-3xl font-bold text-gray-900 mb-2">
                Gestión de Tareas
              </h1>
              {integrationLoading ? (
                <div className="flex items-center text-gray-500" data-testid="integration-status">
                  <div className="animate-spin rounded-full h-4 w-4 border-b-2 border-blue-500 mr-2"></div>
                  Cargando integración...
                </div>
              ) : provider ? (
                <div className="text-gray-600" data-testid="integration-status">
                  <span className="text-green-600 font-semibold">● Conectado</span> con {provider}
                  <br />
                  <span className="text-sm text-gray-500">Última sincronización: hace 5 min • 8 tareas</span>
                </div>
              ) : (
                <p className="text-gray-500" data-testid="integration-status">Sin integración configurada</p>
              )}
            </div>
            
            <div className="flex space-x-3 mt-4 sm:mt-0">
              <button
                onClick={handleRefresh}
                disabled={tasksLoading}
                className="inline-flex items-center px-4 py-2 bg-gray-500 text-white rounded-lg hover:bg-gray-600 focus:outline-none focus:ring-2 focus:ring-gray-500 disabled:opacity-50 disabled:cursor-not-allowed"
              >
                {tasksLoading ? (
                  <div className="animate-spin rounded-full h-4 w-4 border-b-2 border-white mr-2"></div>
                ) : (
                  <svg className="w-4 h-4 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M4 4v5h.582m15.356 2A8.001 8.001 0 004.582 9m0 0H9m11 11v-5h-.581m0 0a8.003 8.003 0 01-15.357-2m15.357 2H15" />
                  </svg>
                )}
                Actualizar
              </button>
              
              <Link
                to="/nueva"
                data-testid="new-task-button"
                className="inline-flex items-center px-4 py-2 bg-blue-500 text-white rounded-lg hover:bg-blue-600 focus:outline-none focus:ring-2 focus:ring-blue-500"
              >
                <svg className="w-4 h-4 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 4v16m8-8H4" />
                </svg>
                Nueva Tarea
              </Link>
            </div>
          </div>
        </div>

        {/* Tasks table */}
        <Table showToast={showToast} />
      </div>

      {/* Toast notifications */}
      <ToastContainer toasts={toasts} removeToast={removeToast} />
    </main>
  )
}

export default TasksPage