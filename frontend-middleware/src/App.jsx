import { BrowserRouter as Router, Routes, Route } from 'react-router-dom'
import { Provider } from 'react-redux'
import store from './app/store.js'
import TasksPage from './pages/TasksPage.jsx'
import NewTaskPage from './pages/NewTaskPage.jsx'
import './index.css'

/**
 * Main application component with routing
 */
function App() {
  return (
    <Provider store={store}>
      <Router>
        <div className="App">
          <Routes>
            <Route path="/" element={<TasksPage />} />
            <Route path="/nueva" element={<NewTaskPage />} />
            {/* Catch all route for 404 */}
            <Route path="*" element={
              <div className="min-h-screen bg-gray-100 flex items-center justify-center">
                <div className="text-center">
                  <h1 className="text-4xl font-bold text-gray-900 mb-4">404</h1>
                  <p className="text-gray-600 mb-6">Página no encontrada</p>
                  <a
                    href="/"
                    className="inline-flex items-center px-4 py-2 bg-blue-500 text-white rounded-lg hover:bg-blue-600 focus:outline-none focus:ring-2 focus:ring-blue-500"
                  >
                    Volver al inicio
                  </a>
                </div>
              </div>
            } />
          </Routes>
        </div>
      </Router>
    </Provider>
  )
}

export default App