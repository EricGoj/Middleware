import { createSlice, createAsyncThunk } from '@reduxjs/toolkit'
import { api } from '../../services/issueApi.js'

/**
 * @typedef {'PENDING'|'IN_PROGRESS'|'DONE'|'CANCELLED'} IssueStatus
 * @typedef {{ id: string; title: string; description: string; dueDate: string; status: IssueStatus; priority: string }} Issue
 */

const initialState = {
  items: [],
  loading: false,
  error: null,
  filters: {
    search: '',
    sortBy: 'createdAt',
    sortOrder: 'desc'
  }
}

// Async thunks for API operations
export const fetchIssues = createAsyncThunk(
  'issues/fetchIssues',
  async (_, { rejectWithValue }) => {
    try {
      const issues = await api.getIssues()
      return issues
    } catch (error) {
      return rejectWithValue(error.response?.data?.message || 'Failed to fetch issues')
    }
  }
)

export const createIssue = createAsyncThunk(
  'issues/createIssue',
  async (issueData, { rejectWithValue }) => {
    try {
      const newIssue = await api.createIssue(issueData)
      return newIssue
    } catch (error) {
      return rejectWithValue(error.response?.data?.message || 'Failed to create issue')
    }
  }
)

export const updateIssue = createAsyncThunk(
  'issues/updateIssue',
  async ({ id, ...issueData }, { rejectWithValue }) => {
    try {
      const updatedIssue = await api.updateIssue(id, issueData)
      return updatedIssue
    } catch (error) {
      return rejectWithValue(error.response?.data?.message || 'Failed to update issue')
    }
  }
)

export const deleteIssue = createAsyncThunk(
  'issues/deleteIssue',
  async (id, { rejectWithValue }) => {
    try {
      await api.deleteIssue(id)
      return id
    } catch (error) {
      return rejectWithValue(error.response?.data?.message || 'Failed to delete issue')
    }
  }
)

export const syncWithJira = createAsyncThunk(
  'issues/syncWithJira',
  async (issueId, { rejectWithValue }) => {
    try {
      const syncResult = await api.syncWithJira(issueId)
      return syncResult
    } catch (error) {
      return rejectWithValue(error.response?.data?.message || 'Failed to sync with Jira')
    }
  }
)

const issuesSlice = createSlice({
  name: 'issues',
  initialState,
  reducers: {
    setFilters: (state, action) => {
      state.filters = { ...state.filters, ...action.payload }
    },
    clearError: (state) => {
      state.error = null
    },
    resetIssues: (state) => {
      state.items = []
      state.error = null
      state.loading = false
    }
  },
  extraReducers: (builder) => {
    builder
      // Fetch issues
      .addCase(fetchIssues.pending, (state) => {
        state.loading = true
        state.error = null
      })
      .addCase(fetchIssues.fulfilled, (state, action) => {
        state.loading = false
        state.items = action.payload
      })
      .addCase(fetchIssues.rejected, (state, action) => {
        state.loading = false
        state.error = action.payload
      })
      
      // Create issue
      .addCase(createIssue.pending, (state) => {
        state.loading = true
        state.error = null
      })
      .addCase(createIssue.fulfilled, (state, action) => {
        state.loading = false
        state.items.push(action.payload)
      })
      .addCase(createIssue.rejected, (state, action) => {
        state.loading = false
        state.error = action.payload
      })
      
      // Update issue
      .addCase(updateIssue.pending, (state) => {
        state.loading = true
        state.error = null
      })
      .addCase(updateIssue.fulfilled, (state, action) => {
        state.loading = false
        const index = state.items.findIndex(issue => issue.id === action.payload.id)
        if (index !== -1) {
          state.items[index] = action.payload
        }
      })
      .addCase(updateIssue.rejected, (state, action) => {
        state.loading = false
        state.error = action.payload
      })
      
      // Delete issue
      .addCase(deleteIssue.pending, (state) => {
        state.loading = true
        state.error = null
      })
      .addCase(deleteIssue.fulfilled, (state, action) => {
        state.loading = false
        state.items = state.items.filter(issue => issue.id !== action.payload)
      })
      .addCase(deleteIssue.rejected, (state, action) => {
        state.loading = false
        state.error = action.payload
      })
      
      // Sync with Jira
      .addCase(syncWithJira.pending, (state) => {
        state.loading = true
        state.error = null
      })
      .addCase(syncWithJira.fulfilled, (state, action) => {
        state.loading = false
        // Update the issue with Jira sync result if needed
        const index = state.items.findIndex(issue => issue.id === action.payload.issueId)
        if (index !== -1 && action.payload.businessKey) {
          state.items[index].businessKey = action.payload.businessKey
        }
      })
      .addCase(syncWithJira.rejected, (state, action) => {
        state.loading = false
        state.error = action.payload
      })
  }
})

export const { setFilters, clearError, resetIssues } = issuesSlice.actions

// Selectors
export const selectIssues = (state) => state.issues.items
export const selectIssuesLoading = (state) => state.issues.loading
export const selectIssuesError = (state) => state.issues.error
export const selectIssuesFilters = (state) => state.issues.filters

export const selectFilteredIssues = (state) => {
  const issues = selectIssues(state)
  const filters = selectIssuesFilters(state)
  
  let filtered = issues
  
  // Apply search filter
  if (filters.search) {
    const searchLower = filters.search.toLowerCase()
    filtered = filtered.filter(issue => 
      issue.title.toLowerCase().includes(searchLower) ||
      issue.description.toLowerCase().includes(searchLower)
    )
  }
  
  // Apply sorting
  filtered.sort((a, b) => {
    const aValue = a[filters.sortBy]
    const bValue = b[filters.sortBy]
    
    if (filters.sortOrder === 'asc') {
      return aValue > bValue ? 1 : -1
    } else {
      return aValue < bValue ? 1 : -1
    }
  })
  
  return filtered
}

export default issuesSlice.reducer