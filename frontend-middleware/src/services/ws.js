import { Client } from '@stomp/stompjs'
import SockJS from 'sockjs-client'
import { upsertFromWs, removeFromWs, fetchTasks } from '../features/tasks/tasksSlice.js'

let stompClient = null
let reconnectAttempts = 0
const MAX_RECONNECT_ATTEMPTS = 5
const INITIAL_RECONNECT_DELAY = 1000

/**
 * Extract task from WebSocket message payload
 * @param {Object} payload - WebSocket message payload
 * @returns {Object} - Task object with backend field names
 */
const extractTaskFromPayload = (payload) => {
  // Return task directly with backend field names (no transformation needed)
  return payload.task || payload
}

/**
 * Connect to WebSocket STOMP server
 * @param {Function} dispatch - Redux dispatch function
 */
export const connectWebSocket = (dispatch) => {
  const wsUrl = import.meta.env.VITE_WS_URL || 'http://localhost:8080/ws'
  
  if (stompClient?.connected) {
    console.log('WebSocket already connected')
    return
  }

  stompClient = new Client({
    // Use SockJS transport to match backend withSockJS()
    webSocketFactory: () => new SockJS(wsUrl),
    connectHeaders: {},
    debug: (str) => {
      console.log('STOMP Debug:', str)
    },
    reconnectDelay: INITIAL_RECONNECT_DELAY * Math.pow(2, reconnectAttempts),
    heartbeatIncoming: 4000,
    heartbeatOutgoing: 4000,
    onConnect: () => {
      console.log('WebSocket connected successfully')
      reconnectAttempts = 0
      
      // Subscribe to Jira events
      stompClient.subscribe('/topic/jira-events', (message) => {
        try {
          const data = JSON.parse(message.body)
          console.log('Received WebSocket message:', data)
          
          switch (data.type) {
            case 'TASK_CREATED':
            case 'TASK_UPDATED':
              if (data.task) {
                const task = extractTaskFromPayload(data)
                dispatch(upsertFromWs(task))
              }
              break
            case 'TASK_DELETED':
              if (data.id) {
                dispatch(removeFromWs(data.id))
              }
              break
            case 'JIRA_ISSUE_CREATED':
            case 'JIRA_ISSUE_UPDATED':
            case 'JIRA_ISSUE_DELETED':
              // Jira webhook events: refresh tasks to reflect latest state
              dispatch(fetchTasks())
              break
            default:
              console.warn('Unknown WebSocket message type:', data.type)
          }
        } catch (error) {
          console.error('Error processing WebSocket message:', error)
        }
      })
    },
    onStompError: (frame) => {
      console.error('STOMP error:', frame.headers['message'])
      console.error('Details:', frame.body)
    },
    onWebSocketError: (error) => {
      console.error('WebSocket error:', error)
    },
    onDisconnect: () => {
      console.log('WebSocket disconnected')
      
      // Attempt to reconnect if not at max attempts
      if (reconnectAttempts < MAX_RECONNECT_ATTEMPTS) {
        reconnectAttempts++
        console.log(`Attempting to reconnect... (${reconnectAttempts}/${MAX_RECONNECT_ATTEMPTS})`)
        setTimeout(() => {
          if (stompClient && !stompClient.connected) {
            stompClient.activate()
          }
        }, INITIAL_RECONNECT_DELAY * Math.pow(2, reconnectAttempts))
      } else {
        console.error('Max reconnection attempts reached')
      }
    }
  })

  stompClient.activate()
  return stompClient
}

/**
 * Disconnect from WebSocket STOMP server
 */
export const disconnectWebSocket = () => {
  if (stompClient?.connected) {
    stompClient.deactivate()
    stompClient = null
    reconnectAttempts = 0
    console.log('WebSocket disconnected manually')
  }
}

/**
 * Get current WebSocket connection status
 * @returns {boolean} - Connection status
 */
export const isWebSocketConnected = () => {
  return stompClient?.connected || false
}