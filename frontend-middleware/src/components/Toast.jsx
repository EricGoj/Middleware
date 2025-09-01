import { useState, useEffect } from 'react'

/**
 * Toast notification component
 * @param {Object} props
 * @param {string} props.message - Toast message
 * @param {string} props.type - Toast type: 'success', 'error', 'info'
 * @param {Function} props.onClose - Callback when toast is closed
 * @param {number} props.duration - Auto-close duration in milliseconds (default: 5000)
 */
const Toast = ({ message, type = 'info', onClose, duration = 5000 }) => {
  const [isVisible, setIsVisible] = useState(true)

  useEffect(() => {
    if (duration > 0) {
      const timer = setTimeout(() => {
        setIsVisible(false)
        setTimeout(onClose, 300) // Wait for animation to complete
      }, duration)

      return () => clearTimeout(timer)
    }
  }, [duration, onClose])

  const getToastClasses = () => {
    const baseClasses = 'fixed top-4 right-4 p-4 rounded-lg shadow-lg transition-all duration-300 z-50 max-w-sm'
    const typeClasses = {
      success: 'bg-green-500 text-white',
      error: 'bg-red-500 text-white',
      info: 'bg-blue-500 text-white',
      warning: 'bg-yellow-500 text-black'
    }
    const visibilityClasses = isVisible ? 'opacity-100 translate-x-0' : 'opacity-0 translate-x-full'
    
    return `${baseClasses} ${typeClasses[type] || typeClasses.info} ${visibilityClasses}`
  }

  const handleClose = () => {
    setIsVisible(false)
    setTimeout(onClose, 300)
  }

  if (!isVisible && duration === 0) {
    return null
  }

  return (
    <div 
      className={getToastClasses()}
      data-testid="toast"
      role="alert"
      aria-live="polite"
      aria-atomic="true"
    >
      <div className="flex items-center justify-between">
        <span className="flex-1 mr-2">{message}</span>
        <button
          onClick={handleClose}
          className="text-white hover:text-gray-200 focus:outline-none focus:ring-2 focus:ring-white focus:ring-offset-2 focus:ring-offset-red-500 rounded"
          aria-label="Cerrar notificación"
        >
          <svg className="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M6 18L18 6M6 6l12 12" />
          </svg>
        </button>
      </div>
    </div>
  )
}

/**
 * Toast container component for managing multiple toasts
 */
export const ToastContainer = ({ toasts, removeToast }) => {
  return (
    <div className="toast-container">
      {toasts.map((toast) => (
        <Toast
          key={toast.id}
          message={toast.message}
          type={toast.type}
          onClose={() => removeToast(toast.id)}
          duration={toast.duration}
        />
      ))}
    </div>
  )
}

export default Toast