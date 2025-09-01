/**
 * Status badge component for displaying task status
 * @param {Object} props
 * @param {string} props.status - Task status: 'TODO', 'IN_PROGRESS', 'DONE'
 * @param {string} props.className - Additional CSS classes
 */
const StatusBadge = ({ status, className = '' }) => {
  const getStatusConfig = () => {
    switch (status) {
      case 'TODO':
        return {
          label: 'Por Hacer',
          classes: 'bg-gray-100 text-gray-800 border-gray-300'
        }
      case 'IN_PROGRESS':
        return {
          label: 'En Progreso',
          classes: 'bg-blue-100 text-blue-800 border-blue-300'
        }
      case 'DONE':
        return {
          label: 'Completada',
          classes: 'bg-green-100 text-green-800 border-green-300'
        }
      default:
        return {
          label: 'Desconocido',
          classes: 'bg-gray-100 text-gray-800 border-gray-300'
        }
    }
  }

  const config = getStatusConfig()
  
  return (
    <span
      className={`inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium border ${config.classes} ${className}`}
      data-testid="status-badge"
    >
      {config.label}
    </span>
  )
}

export default StatusBadge