import { createSlice, createAsyncThunk } from '@reduxjs/toolkit'
import { api } from '../../services/api.js'

const initialState = {
  provider: null,
  loading: false,
  error: null
}

// Async thunk to fetch integration info
export const fetchIntegration = createAsyncThunk(
  'integration/fetchIntegration',
  async (_, { rejectWithValue }) => {
    try {
      const response = await api.getIntegration()
      return response.data
    } catch (error) {
      return rejectWithValue(error.response?.data?.message || 'Error al cargar la integración')
    }
  }
)

const integrationSlice = createSlice({
  name: 'integration',
  initialState,
  reducers: {
    clearError: (state) => {
      state.error = null
    },
  },
  extraReducers: (builder) => {
    builder
      .addCase(fetchIntegration.pending, (state) => {
        state.loading = true
        state.error = null
      })
      .addCase(fetchIntegration.fulfilled, (state, action) => {
        state.loading = false
        state.provider = action.payload.provider
      })
      .addCase(fetchIntegration.rejected, (state, action) => {
        state.loading = false
        state.error = action.payload
      })
  },
})

export const { clearError } = integrationSlice.actions
export default integrationSlice.reducer