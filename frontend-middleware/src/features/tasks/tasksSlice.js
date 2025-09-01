import { createSlice, createAsyncThunk } from '@reduxjs/toolkit'
import { api } from '../../services/api.js'

/**
 * @typedef {'TODO'|'IN_PROGRESS'|'DONE'} TaskStatus
 * @typedef {{ id: string; titulo: string; descripcion: string; fecha: string; estado: TaskStatus; completada: boolean }} Task
 */

const initialState = {
  items: [],
  loading: false,
  error: null,
  filters: {
    search: '',
    sortBy: 'fecha',
    sortOrder: 'desc'
  }
}

// Async thunks for API operations
export const fetchTasks = createAsyncThunk(
  'tasks/fetchTasks',
  async (_, { rejectWithValue }) => {
    try {
      const response = await api.getTasks()
      return response.data
    } catch (error) {
      return rejectWithValue(error.response?.data?.message || 'Error al cargar las tareas')
    }
  }
)

export const createTask = createAsyncThunk(
  'tasks/createTask',
  async (taskData, { rejectWithValue }) => {
    try {
      const response = await api.createTask(taskData)
      return response.data
    } catch (error) {
      return rejectWithValue(error.response?.data?.message || 'Error al crear la tarea')
    }
  }
)

export const updateTask = createAsyncThunk(
  'tasks/updateTask',
  async ({ id, ...taskData }, { rejectWithValue }) => {
    try {
      const response = await api.updateTask(id, taskData)
      return response.data
    } catch (error) {
      return rejectWithValue(error.response?.data?.message || 'Error al actualizar la tarea')
    }
  }
)

export const deleteTask = createAsyncThunk(
  'tasks/deleteTask',
  async (taskId, { rejectWithValue }) => {
    try {
      await api.deleteTask(taskId)
      return taskId
    } catch (error) {
      return rejectWithValue(error.response?.data?.message || 'Error al eliminar la tarea')
    }
  }
)

const tasksSlice = createSlice({
  name: 'tasks',
  initialState,
  reducers: {
    clearError: (state) => {
      state.error = null
    },
    setFilter: (state, action) => {
      state.filters = { ...state.filters, ...action.payload }
    },
    clearFilter: (state) => {
      state.filters = {
        search: '',
        sortBy: 'fecha',
        sortOrder: 'desc'
      }
    },
    toggleCompleteLocal: (state, action) => {
      const task = state.items.find(t => t.id === action.payload)
      if (task) {
        task.completada = !task.completada
        task.estado = task.completada ? 'DONE' : 'TODO'
      }
    },
    // WebSocket event reducers
    upsertFromWs: (state, action) => {
      const task = action.payload
      const index = state.items.findIndex(t => t.id === task.id)
      if (index !== -1) {
        state.items[index] = task
      } else {
        state.items.push(task)
      }
    },
    removeFromWs: (state, action) => {
      const taskId = action.payload
      state.items = state.items.filter(t => t.id !== taskId)
    },
  },
  extraReducers: (builder) => {
    builder
      // Fetch tasks
      .addCase(fetchTasks.pending, (state) => {
        state.loading = true
        state.error = null
      })
      .addCase(fetchTasks.fulfilled, (state, action) => {
        state.loading = false
        state.items = action.payload
      })
      .addCase(fetchTasks.rejected, (state, action) => {
        state.loading = false
        state.error = action.payload
      })
      // Create task
      .addCase(createTask.pending, (state) => {
        state.loading = true
        state.error = null
      })
      .addCase(createTask.fulfilled, (state, action) => {
        state.loading = false
        state.items.push(action.payload)
      })
      .addCase(createTask.rejected, (state, action) => {
        state.loading = false
        state.error = action.payload
      })
      // Update task
      .addCase(updateTask.pending, (state) => {
        state.error = null
      })
      .addCase(updateTask.fulfilled, (state, action) => {
        const index = state.items.findIndex(t => t.id === action.payload.id)
        if (index !== -1) {
          state.items[index] = action.payload
        }
      })
      .addCase(updateTask.rejected, (state, action) => {
        state.error = action.payload
      })
      // Delete task
      .addCase(deleteTask.pending, (state) => {
        state.error = null
      })
      .addCase(deleteTask.fulfilled, (state, action) => {
        state.items = state.items.filter(t => t.id !== action.payload)
      })
      .addCase(deleteTask.rejected, (state, action) => {
        state.error = action.payload
      })
  },
})

export const { clearError, setFilter, clearFilter, toggleCompleteLocal, upsertFromWs, removeFromWs } = tasksSlice.actions
export default tasksSlice.reducer