// Mock data para desarrollo - formato español
export const mockTasks = [
  {
    id: '1',
    titulo: 'Implementar autenticación de usuarios',
    descripcion: 'Crear sistema de login y registro con validación de formularios',
    fecha: '2025-01-15',
    estado: 'IN_PROGRESS',
    completada: false,
    fechaCreacion: '2025-01-10T09:00:00Z',
    fechaActualizacion: '2025-01-12T14:30:00Z'
  },
  {
    id: '2',
    titulo: 'Diseñar dashboard principal',
    descripcion: 'Crear interfaz responsive con métricas y gráficos interactivos',
    fecha: '2025-01-20',
    estado: 'TODO',
    completada: false,
    fechaCreacion: '2025-01-10T10:15:00Z',
    fechaActualizacion: '2025-01-10T10:15:00Z'
  },
  {
    id: '3',
    titulo: 'Configurar base de datos',
    descripcion: 'Establecer conexión MongoDB y definir esquemas de datos',
    fecha: '2025-01-12',
    estado: 'DONE',
    completada: true,
    fechaCreacion: '2025-01-08T16:20:00Z',
    fechaActualizacion: '2025-01-12T11:45:00Z'
  },
  {
    id: '4',
    titulo: 'Optimizar rendimiento frontend',
    descripcion: 'Implementar lazy loading y optimización de bundles para mejorar velocidad',
    fecha: '2025-01-25',
    estado: 'TODO',
    completada: false,
    fechaCreacion: '2025-01-11T13:00:00Z',
    fechaActualizacion: '2025-01-11T13:00:00Z'
  },
  {
    id: '5',
    titulo: 'Integrar notificaciones push',
    descripcion: 'Configurar sistema de notificaciones en tiempo real con WebSocket',
    fecha: '2025-01-18',
    estado: 'IN_PROGRESS',
    completada: false,
    fechaCreacion: '2025-01-09T11:30:00Z',
    fechaActualizacion: '2025-01-14T09:15:00Z'
  },
  {
    id: '6',
    titulo: 'Implementar tests automatizados',
    descripcion: 'Crear suite completa de pruebas unitarias y de integración',
    fecha: '2025-01-30',
    estado: 'TODO',
    completada: false,
    fechaCreacion: '2025-01-12T15:45:00Z',
    fechaActualizacion: '2025-01-12T15:45:00Z'
  },
  {
    id: '7',
    titulo: 'Configurar CI/CD pipeline',
    descripcion: 'Automatizar despliegue y testing con GitHub Actions',
    fecha: '2025-02-05',
    estado: 'BLOCKED',
    completada: false,
    fechaCreacion: '2025-01-13T12:00:00Z',
    fechaActualizacion: '2025-01-13T12:00:00Z'
  },
  {
    id: '8',
    titulo: 'Documentar API endpoints',
    descripcion: 'Crear documentación completa con Swagger/OpenAPI',
    fecha: '2025-01-22',
    estado: 'TODO',
    completada: false,
    fechaCreacion: '2025-01-11T17:20:00Z',
    fechaActualizacion: '2025-01-11T17:20:00Z'
  }
]

// Mock para datos de integración
export const mockIntegration = {
  jiraConnected: true,
  lastSync: '2025-01-15T10:30:00Z',
  syncStatus: 'active',
  tasksCount: 8,
  pendingSync: 2
}

// Mock para estados posibles
export const mockEstados = [
  { value: 'TODO', label: 'Por hacer', color: 'bg-gray-100 text-gray-800' },
  { value: 'IN_PROGRESS', label: 'En progreso', color: 'bg-blue-100 text-blue-800' },
  { value: 'DONE', label: 'Completada', color: 'bg-green-100 text-green-800' },
  { value: 'BLOCKED', label: 'Bloqueada', color: 'bg-red-100 text-red-800' }
]

// Utilitarios para generar datos mock dinámicos
export const generateMockTask = (overrides = {}) => {
  const baseTask = {
    id: `mock-${Date.now()}`,
    titulo: 'Nueva tarea mock',
    descripcion: 'Descripción generada automáticamente para testing',
    fecha: new Date(Date.now() + 7 * 24 * 60 * 60 * 1000).toISOString().split('T')[0],
    estado: 'TODO',
    completada: false,
    fechaCreacion: new Date().toISOString(),
    fechaActualizacion: new Date().toISOString()
  }
  
  return { ...baseTask, ...overrides }
}

export const getRandomMockTask = () => {
  const titles = [
    'Revisar código frontend',
    'Actualizar dependencias',
    'Corregir bug en formulario',
    'Mejorar accesibilidad',
    'Optimizar consultas SQL'
  ]
  
  const descriptions = [
    'Realizar revisión completa del código y aplicar mejores prácticas',
    'Actualizar todas las dependencias a sus últimas versiones estables',
    'Corregir problema de validación en el formulario de nueva tarea',
    'Implementar mejoras de accesibilidad según estándares WCAG',
    'Optimizar consultas de base de datos para mejorar rendimiento'
  ]
  
  const estados = ['TODO', 'IN_PROGRESS', 'DONE', 'BLOCKED']
  
  return generateMockTask({
    titulo: titles[Math.floor(Math.random() * titles.length)],
    descripcion: descriptions[Math.floor(Math.random() * descriptions.length)],
    estado: estados[Math.floor(Math.random() * estados.length)]
  })
}