import { useState } from 'react'
import { useDispatch } from 'react-redux'
import { updateTask, deleteTask } from '../features/tasks/tasksSlice.js'
import StatusBadge from './StatusBadge.jsx'

/**
 * Task row component with inline editing capabilities
 * @param {Object} props
 * @param {Object} props.task - Task object
 * @param {Function} props.onDelete - Delete confirmation callback
 * @param {Function} props.showToast - Toast notification callback
 */
const TaskRow = ({ task, onDelete, showToast }) => {
  const dispatch = useDispatch()
  const [isEditing, setIsEditing] = useState(false)
  const [editData, setEditData] = useState({
    titulo: task.titulo,
    descripcion: task.descripcion,
    fecha: task.fecha,
    estado: task.estado
  })

  const handleEdit = () => {
    setIsEditing(true)
  }

  const handleSave = async () => {
    try {
      await dispatch(updateTask({ id: task.id, ...editData })).unwrap()
      setIsEditing(false)
      showToast('Tarea actualizada correctamente', 'success')
    } catch (error) {
      showToast(error || 'Error al actualizar la tarea', 'error')
    }
  }

  const handleCancel = () => {
    setEditData({
      titulo: task.titulo,
      descripcion: task.descripcion,
      fecha: task.fecha,
      estado: task.estado
    })
    setIsEditing(false)
  }

  const handleToggleComplete = async () => {
    const newStatus = task.completada ? 'TODO' : 'DONE'
    try {
      await dispatch(updateTask({ 
        id: task.id, 
        estado: newStatus,
        completada: !task.completada
      })).unwrap()
      showToast(
        task.completada ? 'Tarea marcada como pendiente' : 'Tarea completada',
        'success'
      )
    } catch (error) {
      showToast(error || 'Error al actualizar el estado', 'error')
    }
  }

  const handleDeleteClick = () => {
    onDelete(task)
  }

  const formatDate = (dateString) => {
    if (!dateString) return '-'
    try {
      return new Date(dateString).toLocaleDateString('es-ES')
    } catch {
      return dateString
    }
  }

  const handleInputChange = (field, value) => {
    setEditData(prev => ({ ...prev, [field]: value }))
  }

  if (isEditing) {
    return (
      <tr className="border-b hover:bg-gray-50">
        <td className="p-3">
          <input
            type="text"
            value={editData.titulo}
            onChange={(e) => handleInputChange('titulo', e.target.value)}
            data-testid="edit-titulo"
            className="w-full p-2 border border-gray-300 rounded focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
            placeholder="Título de la tarea"
          />
        </td>
        <td className="p-3">
          <textarea
            value={editData.descripcion}
            onChange={(e) => handleInputChange('descripcion', e.target.value)}
            data-testid="edit-descripcion"
            className="w-full p-2 border border-gray-300 rounded focus:ring-2 focus:ring-blue-500 focus:border-blue-500 resize-none"
            rows={2}
            placeholder="Descripción"
          />
        </td>
        <td className="p-3">
          <input
            type="date"
            value={editData.fecha ? editData.fecha.split('T')[0] : ''}
            onChange={(e) => handleInputChange('fecha', e.target.value)}
            className="w-full p-2 border border-gray-300 rounded focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
          />
        </td>
        <td className="p-3">
          <select
            value={editData.estado}
            onChange={(e) => handleInputChange('estado', e.target.value)}
            data-testid="edit-estado"
            className="w-full p-2 border border-gray-300 rounded focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
          >
            <option value="TODO">Por Hacer</option>
            <option value="IN_PROGRESS">En Progreso</option>
            <option value="DONE">Completada</option>
          </select>
        </td>
        <td className="p-3">
          <div className="flex space-x-2">
            <button
              onClick={handleSave}
              data-testid="save-button"
              className="px-3 py-1 bg-green-500 text-white rounded hover:bg-green-600 focus:outline-none focus:ring-2 focus:ring-green-500"
            >
              Guardar
            </button>
            <button
              onClick={handleCancel}
              className="px-3 py-1 bg-gray-500 text-white rounded hover:bg-gray-600 focus:outline-none focus:ring-2 focus:ring-gray-500"
            >
              Cancelar
            </button>
          </div>
        </td>
      </tr>
    )
  }

  return (
    <tr className="border-b hover:bg-gray-50" data-testid="task-row">
      <td className="p-3">
        <div className="flex items-center space-x-2">
          <input
            type="checkbox"
            checked={task.completada}
            onChange={handleToggleComplete}
            className="w-4 h-4 text-blue-600 bg-gray-100 border-gray-300 rounded focus:ring-blue-500 focus:ring-2"
            aria-label={`Marcar como ${task.completada ? 'pendiente' : 'completada'}`}
          />
          <span className={task.completada ? 'line-through text-gray-500' : ''} data-testid="task-title">
            {task.titulo}
          </span>
        </div>
      </td>
      <td className="p-3 text-gray-600">
        <div className="max-w-xs truncate" title={task.descripcion}>
          {task.descripcion || '-'}
        </div>
      </td>
      <td className="p-3 text-gray-600">
        {formatDate(task.fecha)}
      </td>
      <td className="p-3">
        <StatusBadge status={task.estado} />
      </td>
      <td className="p-3">
        <div className="flex space-x-2">
          <button
            onClick={handleEdit}
            data-testid="edit-button"
            className="px-3 py-1 bg-blue-500 text-white rounded hover:bg-blue-600 focus:outline-none focus:ring-2 focus:ring-blue-500"
            aria-label="Editar tarea"
          >
            Editar
          </button>
          <button
            onClick={handleDeleteClick}
            data-testid="delete-button"
            className="px-3 py-1 bg-red-500 text-white rounded hover:bg-red-600 focus:outline-none focus:ring-2 focus:ring-red-500"
            aria-label="Eliminar tarea"
          >
            Eliminar
          </button>
        </div>
      </td>
    </tr>
  )
}

export default TaskRow