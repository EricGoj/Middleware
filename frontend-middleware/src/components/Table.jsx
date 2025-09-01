import { useState } from 'react'
import { useSelector, useDispatch } from 'react-redux'
import { setFilter, deleteTask } from '../features/tasks/tasksSlice.js'
import TaskRow from './TaskRow.jsx'

/**
 * Interactive table component for displaying and managing tasks
 * @param {Object} props
 * @param {Function} props.showToast - Toast notification callback
 */
const Table = ({ showToast }) => {
  const dispatch = useDispatch()
  const { items: tasks, loading, filters } = useSelector(state => state.tasks)
  const [deleteConfirm, setDeleteConfirm] = useState(null)

  // Filter and sort tasks based on current filters
  const filteredTasks = tasks
    .filter(task => {
      // Text search filter
      const matchesSearch = task.titulo.toLowerCase().includes(filters.search.toLowerCase()) ||
        task.descripcion.toLowerCase().includes(filters.search.toLowerCase())
      
      // Status filter
      const matchesStatus = !filters.status || task.estado === filters.status
      
      return matchesSearch && matchesStatus
    })
    .sort((a, b) => {
      let aValue = a[filters.sortBy]
      let bValue = b[filters.sortBy]
      
      // Handle date sorting
      if (filters.sortBy === 'fecha') {
        aValue = new Date(aValue || 0).getTime()
        bValue = new Date(bValue || 0).getTime()
      }
      
      if (filters.sortOrder === 'asc') {
        return aValue > bValue ? 1 : -1
      } else {
        return aValue < bValue ? 1 : -1
      }
    })

  const handleSearchChange = (e) => {
    dispatch(setFilter({ search: e.target.value }))
  }

  const handleStatusFilterChange = (e) => {
    dispatch(setFilter({ status: e.target.value }))
  }

  const handleSortChange = (field) => {
    const newOrder = filters.sortBy === field && filters.sortOrder === 'asc' ? 'desc' : 'asc'
    dispatch(setFilter({ sortBy: field, sortOrder: newOrder }))
  }

  const handleDeleteConfirm = async (task) => {
    try {
      await dispatch(deleteTask(task.id)).unwrap()
      setDeleteConfirm(null)
      showToast('Tarea eliminada correctamente', 'success')
    } catch (error) {
      showToast(error || 'Error al eliminar la tarea', 'error')
    }
  }

  const getSortIcon = (field) => {
    if (filters.sortBy !== field) {
      return (
        <svg className="w-4 h-4 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M7 16V4m0 0L3 8m4-4l4 4m6 0v12m0 0l4-4m-4 4l-4-4" />
        </svg>
      )
    }
    
    if (filters.sortOrder === 'asc') {
      return (
        <svg className="w-4 h-4 text-blue-500" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M5 15l7-7 7 7" />
        </svg>
      )
    } else {
      return (
        <svg className="w-4 h-4 text-blue-500" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M19 9l-7 7-7-7" />
        </svg>
      )
    }
  }

  if (loading) {
    return (
      <div className="flex justify-center items-center h-64">
        <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-500"></div>
      </div>
    )
  }

  return (
    <div className="bg-white rounded-lg shadow overflow-hidden">
      {/* Search and filter bar */}
      <div className="p-4 border-b border-gray-200">
        <div className="flex flex-col sm:flex-row gap-4">
          <div className="flex-1">
            <input
              type="text"
              placeholder="Buscar tareas..."
              value={filters.search}
              onChange={handleSearchChange}
              data-testid="search-input"
              className="w-full p-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
            />
          </div>
          <div className="sm:w-48">
            <select
              value={filters.status || ''}
              onChange={handleStatusFilterChange}
              data-testid="status-filter"
              aria-label="Filtrar por estado"
              className="w-full p-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
            >
              <option value="">Todos los estados</option>
              <option value="TODO">Por Hacer</option>
              <option value="IN_PROGRESS">En Progreso</option>
              <option value="DONE">Completada</option>
              <option value="BLOCKED">Bloqueada</option>
            </select>
          </div>
        </div>
      </div>

      {/* Table */}
      <div className="overflow-x-auto">
        <table className="w-full" data-testid="tasks-table">
          <thead className="bg-gray-50 sticky top-0">
            <tr>
              <th className="text-left p-3 font-medium text-gray-700">
                <button
                  onClick={() => handleSortChange('titulo')}
                  className="flex items-center space-x-1 hover:text-blue-600 focus:outline-none focus:text-blue-600"
                >
                  <span>Título</span>
                  {getSortIcon('titulo')}
                </button>
              </th>
              <th className="text-left p-3 font-medium text-gray-700">Descripción</th>
              <th className="text-left p-3 font-medium text-gray-700">
                <button
                  onClick={() => handleSortChange('fecha')}
                  className="flex items-center space-x-1 hover:text-blue-600 focus:outline-none focus:text-blue-600"
                >
                  <span>Fecha</span>
                  {getSortIcon('fecha')}
                </button>
              </th>
              <th className="text-left p-3 font-medium text-gray-700">Estado</th>
              <th className="text-left p-3 font-medium text-gray-700">Acciones</th>
            </tr>
          </thead>
          <tbody>
            {filteredTasks.length === 0 ? (
              <tr>
                <td colSpan={5} className="p-8 text-center text-gray-500">
                  {filters.search ? 'No se encontraron tareas que coincidan con la búsqueda' : 'No hay tareas disponibles'}
                </td>
              </tr>
            ) : (
              filteredTasks.map((task) => (
                <TaskRow
                  key={task.id}
                  task={task}
                  onDelete={(task) => setDeleteConfirm(task)}
                  showToast={showToast}
                />
              ))
            )}
          </tbody>
        </table>
      </div>

      {/* Delete confirmation modal */}
      {deleteConfirm && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50" data-testid="delete-modal">
          <div className="bg-white p-6 rounded-lg max-w-md w-full mx-4">
            <h3 className="text-lg font-semibold mb-4">Confirmar eliminación</h3>
            <p className="text-gray-600 mb-6">
              ¿Estás seguro de que deseas eliminar la tarea "{deleteConfirm.titulo}"?
              Esta acción no se puede deshacer.
            </p>
            <div className="flex justify-end space-x-3">
              <button
                onClick={() => setDeleteConfirm(null)}
                className="px-4 py-2 bg-gray-300 text-gray-700 rounded hover:bg-gray-400 focus:outline-none focus:ring-2 focus:ring-gray-500"
              >
                Cancelar
              </button>
              <button
                onClick={() => handleDeleteConfirm(deleteConfirm)}
                data-testid="confirm-delete"
                className="px-4 py-2 bg-red-500 text-white rounded hover:bg-red-600 focus:outline-none focus:ring-2 focus:ring-red-500"
              >
                Eliminar
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  )
}

export default Table