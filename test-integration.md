# Integration Testing Guide

## 🧪 Testing the Fixed Backend-Frontend Integration

### Prerequisites
1. Backend running on `http://localhost:8080`
2. Frontend running on `http://localhost:5173`
3. PostgreSQL database running on `localhost:5432`

### Quick Integration Test

#### 1. Test REST API Endpoints

**Check if backend is running:**
```bash
curl -X GET http://localhost:8080/api/issues
```

**Expected Response:**
```json
[
  {
    "id": "uuid-here",
    "title": "Sample Issue",
    "description": "Issue description",
    "status": "PENDING",
    "createdAt": "2025-01-XX...",
    "updatedAt": "2025-01-XX...",
    "dueDate": null,
    "priority": "MEDIUM"
  }
]
```

**Create a new issue:**
```bash
curl -X POST http://localhost:8080/api/issues \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Test Integration Issue",
    "description": "Testing the fixed integration",
    "priority": "HIGH"
  }'
```

#### 2. Test WebSocket Connection

**Frontend Console Test:**
1. Open browser developer tools
2. Navigate to `http://localhost:5173`
3. Check console for WebSocket connection messages:
   - ✅ `"WebSocket connected successfully"`
   - ✅ `"STOMP Debug: ..."` messages
   - ❌ No connection errors

**WebSocket Event Test:**
1. Create/update an issue via REST API
2. Check frontend console for WebSocket events:
   - ✅ `"Received WebSocket message: {type: 'TASK_CREATED', task: {...}}"`
   - ✅ Redux state should update automatically

### Integration Verification Checklist

- [ ] **REST API Communication**
  - [ ] GET `/api/issues` returns issue list
  - [ ] POST `/api/issues` creates new issues
  - [ ] PATCH `/api/issues/{id}` updates issues
  - [ ] DELETE `/api/issues/{id}` removes issues
  - [ ] No 404 errors for `/api/tasks` (old endpoints)

- [ ] **WebSocket Real-time Updates**
  - [ ] Frontend connects to `/ws` endpoint
  - [ ] Backend publishes to `/topic/jira-events`
  - [ ] Frontend receives `TASK_CREATED` events
  - [ ] Frontend receives `TASK_UPDATED` events
  - [ ] Frontend receives `TASK_DELETED` events
  - [ ] Redux state updates automatically

- [ ] **Data Consistency**
  - [ ] Backend uses: `id`, `title`, `description`, `status`, `dueDate`, `priority`
  - [ ] Frontend receives same field names (no transformation)
  - [ ] WebSocket events match REST API data structure
  - [ ] No field mapping errors

- [ ] **CORS Configuration**
  - [ ] Frontend (`localhost:5173`) can access backend
  - [ ] No CORS errors in browser console
  - [ ] WebSocket connections allowed

### Common Issues & Fixes

#### Issue: 404 for `/api/issues`
**Cause:** Backend not running or controller not loaded
**Fix:** Start backend with `./mvnw spring-boot:run`

#### Issue: WebSocket connection refused
**Cause:** Backend WebSocket not configured or CORS issue
**Fix:** Verify `WebSocketConfig.java` and `CorsConfig.java`

#### Issue: Events not received in frontend
**Cause:** Topic mismatch or message format issue
**Fix:** Check `/topic/jira-events` subscription and message format

#### Issue: Field mapping errors
**Cause:** Frontend expecting old field names
**Fix:** Update React components to use backend field names

### Expected Results After Fixes

1. **✅ All REST operations work correctly**
2. **✅ Real-time updates via WebSocket function**
3. **✅ No endpoint mismatches (404 errors eliminated)**
4. **✅ Consistent data format across all layers**
5. **✅ Frontend automatically updates when backend changes**

### Performance Verification

- API response times < 500ms
- WebSocket connection established < 2s
- Real-time updates delivered < 100ms
- No memory leaks in long-running sessions

### Next Steps for Production

1. Add authentication to API endpoints
2. Implement proper error handling and user feedback
3. Add comprehensive integration tests
4. Set up monitoring and logging
5. Configure production CORS settings