import { configureStore } from '@reduxjs/toolkit'
import tasksReducer from '../features/tasks/tasksSlice.js'
import integrationReducer from '../features/integration/integrationSlice.js'

export const store = configureStore({
  reducer: {
    tasks: tasksReducer,
    integration: integrationReducer,
  },
})

export default store