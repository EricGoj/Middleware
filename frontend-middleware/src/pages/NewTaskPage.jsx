import { useState } from 'react'
import { useDispatch } from 'react-redux'
import { useNavigate, Link } from 'react-router-dom'
import { createTask } from '../features/tasks/tasksSlice.js'

/**
 * Form validation helper
 * @param {Object} data - Form data to validate
 * @returns {Object} - Validation errors object
 */
const validateForm = (data) => {
  const errors = {}

  // Título validation
  if (!data.titulo) {
    errors.titulo = 'El título es obligatorio'
  } else if (data.titulo.length < 3) {
    errors.titulo = 'El título debe tener al menos 3 caracteres'
  } else if (data.titulo.length > 80) {
    errors.titulo = 'El título no puede exceder 80 caracteres'
  }

  // Descripción validation
  if (!data.descripcion) {
    errors.descripcion = 'La descripción es obligatoria'
  } else if (data.descripcion.length < 10) {
    errors.descripcion = 'La descripción debe tener al menos 10 caracteres'
  } else if (data.descripcion.length > 500) {
    errors.descripcion = 'La descripción no puede exceder 500 caracteres'
  }

  // Fecha validation
  if (data.fecha) {
    const date = new Date(data.fecha)
    const minDate = new Date('1970-01-01')
    if (isNaN(date.getTime())) {
      errors.fecha = 'Fecha inválida'
    } else if (date < minDate) {
      errors.fecha = 'La fecha no puede ser anterior a 1970-01-01'
    }
  }

  // Estado validation
  const validStates = ['TODO', 'IN_PROGRESS', 'DONE']
  if (!validStates.includes(data.estado)) {
    errors.estado = 'Estado inválido'
  }

  return errors
}

/**
 * New task creation page component
 */
const NewTaskPage = () => {
  const dispatch = useDispatch()
  const navigate = useNavigate()
  const [loading, setLoading] = useState(false)
  const [errors, setErrors] = useState({})
  const [formData, setFormData] = useState({
    titulo: '',
    descripcion: '',
    fecha: '',
    estado: 'TODO'
  })

  const handleInputChange = (field, value) => {
    setFormData(prev => ({ ...prev, [field]: value }))
    
    // Clear error for this field when user starts typing
    if (errors[field]) {
      setErrors(prev => ({ ...prev, [field]: null }))
    }
  }

  const handleSubmit = async (e) => {
    e.preventDefault()
    
    // Validate form
    const validationErrors = validateForm(formData)
    setErrors(validationErrors)
    
    if (Object.keys(validationErrors).length > 0) {
      // Focus on first error field
      const firstErrorField = Object.keys(validationErrors)[0]
      const fieldElement = document.querySelector(`[name="${firstErrorField}"]`)
      if (fieldElement) {
        fieldElement.focus()
      }
      return
    }

    setLoading(true)
    
    try {
      // Create task with completada based on estado
      const taskData = {
        ...formData,
        completada: formData.estado === 'DONE'
      }
      
      await dispatch(createTask(taskData)).unwrap()
      
      // Navigate back to main page and show success message
      navigate('/', { 
        state: { 
          toast: { 
            message: 'Tarea creada correctamente', 
            type: 'success' 
          } 
        } 
      })
    } catch (error) {
      setErrors({ submit: error || 'Error al crear la tarea' })
      setLoading(false)
    }
  }

  const getInputClasses = (field) => {
    const baseClasses = 'w-full p-3 border rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500'
    const errorClasses = errors[field] ? 'border-red-500' : 'border-gray-300'
    return `${baseClasses} ${errorClasses}`
  }

  return (
    <main className="min-h-screen bg-gray-100">
      <div className="container mx-auto px-4 py-8">
        {/* Header */}
        <div className="mb-8">
          <div className="flex items-center justify-between">
            <h1 className="text-3xl font-bold text-gray-900">Nueva Tarea</h1>
            <Link
              to="/"
              className="inline-flex items-center px-4 py-2 bg-gray-500 text-white rounded-lg hover:bg-gray-600 focus:outline-none focus:ring-2 focus:ring-gray-500"
            >
              <svg className="w-4 h-4 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M10 19l-7-7m0 0l7-7m-7 7h18" />
              </svg>
              Volver al listado
            </Link>
          </div>
        </div>

        {/* Form */}
        <div className="bg-white rounded-lg shadow p-6 max-w-2xl mx-auto">
          <form onSubmit={handleSubmit} className="space-y-6" data-testid="new-task-form">
            {/* Global error */}
            {errors.submit && (
              <div className="bg-red-50 border border-red-200 rounded-lg p-4">
                <div className="flex">
                  <svg className="w-5 h-5 text-red-400 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 8v4m0 4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
                  </svg>
                  <span className="text-red-700">{errors.submit}</span>
                </div>
              </div>
            )}

            {/* Título field */}
            <div>
              <label htmlFor="titulo" className="block text-sm font-medium text-gray-700 mb-2">
                Título <span className="text-red-500">*</span>
              </label>
              <input
                type="text"
                id="titulo"
                name="titulo"
                value={formData.titulo}
                onChange={(e) => handleInputChange('titulo', e.target.value)}
                data-testid="titulo-input"
                className={getInputClasses('titulo')}
                placeholder="Ingresa el título de la tarea"
                required
                aria-describedby={errors.titulo ? 'titulo-error' : undefined}
              />
              {errors.titulo && (
                <p id="titulo-error" data-testid="titulo-error" className="mt-1 text-sm text-red-600">
                  {errors.titulo}
                </p>
              )}
            </div>

            {/* Descripción field */}
            <div>
              <label htmlFor="descripcion" className="block text-sm font-medium text-gray-700 mb-2">
                Descripción
              </label>
              <textarea
                id="descripcion"
                name="descripcion"
                value={formData.descripcion}
                onChange={(e) => handleInputChange('descripcion', e.target.value)}
                data-testid="descripcion-input"
                className={getInputClasses('descripcion')}
                placeholder="Describe la tarea (opcional)"
                rows={4}
                aria-describedby={errors.descripcion ? 'descripcion-error' : undefined}
              />
              {errors.descripcion && (
                <p id="descripcion-error" data-testid="descripcion-error" className="mt-1 text-sm text-red-600">
                  {errors.descripcion}
                </p>
              )}
              <p className="mt-1 text-sm text-gray-500">
                {formData.descripcion.length}/500 caracteres
              </p>
            </div>

            {/* Fecha field */}
            <div>
              <label htmlFor="fecha" className="block text-sm font-medium text-gray-700 mb-2">
                Fecha de vencimiento
              </label>
              <input
                type="date"
                id="fecha"
                name="fecha"
                value={formData.fecha}
                onChange={(e) => handleInputChange('fecha', e.target.value)}
                data-testid="fecha-input"
                className={getInputClasses('fecha')}
                min="1970-01-01"
                aria-describedby={errors.fecha ? 'fecha-error' : undefined}
              />
              {errors.fecha && (
                <p id="fecha-error" className="mt-1 text-sm text-red-600">
                  {errors.fecha}
                </p>
              )}
            </div>

            {/* Estado field */}
            <div>
              <label htmlFor="estado" className="block text-sm font-medium text-gray-700 mb-2">
                Estado inicial
              </label>
              <select
                id="estado"
                name="estado"
                value={formData.estado}
                onChange={(e) => handleInputChange('estado', e.target.value)}
                data-testid="estado-select"
                className={getInputClasses('estado')}
                aria-describedby={errors.estado ? 'estado-error' : undefined}
              >
                <option value="TODO">Por Hacer</option>
                <option value="IN_PROGRESS">En Progreso</option>
                <option value="DONE">Completada</option>
              </select>
              {errors.estado && (
                <p id="estado-error" className="mt-1 text-sm text-red-600">
                  {errors.estado}
                </p>
              )}
            </div>

            {/* Submit button */}
            <div className="flex justify-end space-x-3 pt-6">
              <Link
                to="/"
                data-testid="cancel-button"
                className="px-6 py-3 bg-gray-300 text-gray-700 rounded-lg hover:bg-gray-400 focus:outline-none focus:ring-2 focus:ring-gray-500"
              >
                Cancelar
              </Link>
              <button
                type="submit"
                disabled={loading}
                data-testid="submit-button"
                className="px-6 py-3 bg-blue-500 text-white rounded-lg hover:bg-blue-600 focus:outline-none focus:ring-2 focus:ring-blue-500 disabled:opacity-50 disabled:cursor-not-allowed"
              >
                {loading ? (
                  <div className="flex items-center">
                    <div className="animate-spin rounded-full h-4 w-4 border-b-2 border-white mr-2"></div>
                    Creando...
                  </div>
                ) : (
                  'Crear Tarea'
                )}
              </button>
            </div>
          </form>
        </div>
      </div>
    </main>
  )
}

export default NewTaskPage