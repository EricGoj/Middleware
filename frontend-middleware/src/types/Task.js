/**
 * @fileoverview Type definitions for Task-related objects
 * Centralized type definitions for better maintainability and IDE support
 */

/**
 * @typedef {'TODO'|'IN_PROGRESS'|'DONE'} TaskStatus
 * Task status enumeration representing the current state of a task
 */

/**
 * @typedef {Object} Task
 * @property {string} id - Unique identifier for the task
 * @property {string} titulo - Task title/summary
 * @property {string} descripcion - Detailed task description
 * @property {string} fecha - Due date in ISO string format
 * @property {TaskStatus} estado - Current task status
 * @property {boolean} completada - Whether the task is completed (derived from status)
 */

/**
 * @typedef {Object} TaskFormData
 * @property {string} titulo - Task title (required, 3-80 chars)
 * @property {string} descripcion - Task description (optional, max 500 chars)  
 * @property {string} fecha - Due date in ISO format (required)
 * @property {TaskStatus} estado - Initial task status (defaults to TODO)
 */

/**
 * @typedef {Object} TaskFilters
 * @property {string} search - Search term for title/description filtering
 * @property {'fecha'|'titulo'} sortBy - Sort field
 * @property {'asc'|'desc'} sortOrder - Sort direction
 */

/**
 * @typedef {Object} TasksState
 * @property {Task[]} items - Array of tasks
 * @property {boolean} loading - Loading state for async operations
 * @property {string|null} error - Current error message, if any
 * @property {TaskFilters} filters - Current filter/sort configuration
 */

export {
  /* Type exports are for JSDoc - no actual exports needed in JS */
}