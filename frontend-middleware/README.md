# Gestor de Tareas - Frontend Middleware

Una aplicación de gestión de tareas construida con React, Redux Toolkit y Tailwind CSS, con integración a Jira a través de una API REST y WebSocket STOMP para actualizaciones en tiempo real.

## 🚀 Tecnologías

- **React 18** - Biblioteca de UI
- **Redux Toolkit** - Gestión de estado
- **React Router Dom** - Navegación
- **Tailwind CSS** - Estilos y diseño responsive
- **@stomp/stompjs** - Cliente WebSocket STOMP
- **Axios** - Cliente HTTP
- **Vite** - Herramienta de construcción
- **Playwright** - Pruebas E2E automatizadas

## 📁 Estructura del Proyecto

```
frontend-middleware/
├── public/
├── src/
│   ├── app/
│   │   └── store.js                 # Configuración del store Redux
│   ├── features/
│   │   ├── tasks/
│   │   │   └── tasksSlice.js        # Slice de tareas con async thunks
│   │   └── integration/
│   │       └── integrationSlice.js  # Slice de integración
│   ├── components/
│   │   ├── Table.jsx                # Tabla interactiva de tareas
│   │   ├── TaskRow.jsx              # Fila de tarea con edición inline
│   │   ├── StatusBadge.jsx          # Badge de estado con colores
│   │   └── Toast.jsx                # Sistema de notificaciones
│   ├── pages/
│   │   ├── TasksPage.jsx            # Página principal del gestor
│   │   └── NewTaskPage.jsx          # Formulario de nueva tarea
│   ├── services/
│   │   ├── api.js                   # Cliente API con transformaciones
│   │   └── ws.js                    # Cliente WebSocket STOMP
│   ├── mocks/
│   │   ├── data.js                  # Datos mock para desarrollo
│   │   ├── apiService.js            # API mock service
│   │   └── index.js                 # Sistema de mocks
│   ├── App.jsx                      # Componente principal con rutas
│   ├── main.jsx                     # Punto de entrada
│   └── index.css                    # Estilos globales
├── tests/
│   └── e2e/                         # Pruebas E2E con Playwright
│       ├── basic.spec.js            # Tests básicos
│       ├── core-functionality.spec.js # Tests principales ✅
│       ├── tasks.spec.js            # Tests de gestión de tareas
│       ├── newTask.spec.js          # Tests de formulario
│       ├── workflows.spec.js        # Tests de flujos completos
│       ├── accessibility.spec.js    # Tests de accesibilidad
│       └── README.md                # Documentación de tests
├── package.json
├── vite.config.js
├── tailwind.config.js
├── postcss.config.js
├── playwright.config.js             # Configuración Playwright
├── .env.example
└── .env.development                 # Config mocks desarrollo
```

## ⚙️ Configuración

### Variables de Entorno

Crea un archivo `.env` basado en `.env.example`:

```bash
VITE_API_BASE_URL=http://localhost:8080
VITE_WS_URL=ws://localhost:8080/ws
```

### Instalación y Ejecución

```bash
# Instalar dependencias
npm install

# Ejecutar en modo desarrollo
npm run dev

# Construir para producción
npm run build

# Previsualizar build de producción
npm run preview

# Ejecutar linter
npm run lint

# Ejecutar tests
npm run test
```

## 🎯 Funcionalidades

### Pantalla Principal (`/`)
- **Título**: "Gestor de Tareas"
- **Integración**: Muestra el proveedor conectado (ej. "JIRA")
- **Tabla interactiva** con:
  - Búsqueda por título y descripción
  - Ordenamiento por título y fecha (ascendente/descendente)
  - Edición inline de título, descripción, fecha y estado
  - Checkbox para marcar como completa/pendiente
  - Botón de eliminación con confirmación
- **Actualizaciones en tiempo real** vía WebSocket
- **Botón "Agregar Nueva Tarea"** que navega a `/nueva`

### Pantalla Nueva Tarea (`/nueva`)
- **Formulario** con validaciones en cliente:
  - `titulo`: Obligatorio, 3-80 caracteres
  - `descripcion`: Opcional, 0-500 caracteres
  - `fecha`: Fecha ISO válida, no anterior a 1970-01-01
  - `estado`: TODO | IN_PROGRESS | DONE
- **Validación en tiempo real** con mensajes de error por campo
- **Redirección** a `/` con toast de éxito tras crear
- **Manejo de errores** con mensajes comprensibles

## 🔌 Integración con Backend

### API REST

La aplicación se conecta a los siguientes endpoints (con transformación de datos):

- `GET /api/integration` → `{ provider: "JIRA" }`
- `GET /api/tasks` → `Task[]`
- `POST /api/tasks` → Crea nueva tarea
- `PATCH /api/tasks/:id` → Actualiza tarea existente
- `DELETE /api/tasks/:id` → Elimina tarea

### Transformación de Datos

El servicio API maneja la transformación entre el formato del frontend (español) y el backend (inglés):

**Frontend → Backend:**
- `titulo` → `title`
- `descripcion` → `description`
- `fecha` → `dueDate`
- `estado` → `status`
- `completada` → calculado desde `status`

### WebSocket STOMP

- **Conexión**: `VITE_WS_URL`
- **Suscripción**: `/topic/jira-events`
- **Eventos soportados**:
  - `TASK_CREATED` - Nueva tarea desde Jira
  - `TASK_UPDATED` - Tarea actualizada en Jira
  - `TASK_DELETED` - Tarea eliminada en Jira
- **Reconexión automática** con backoff exponencial

## 🎨 Experiencia de Usuario

### Diseño
- **Responsive**: Adaptado para desktop y mobile
- **Accesibilidad**: Labels asociadas, `aria-live` para toasts, gestión de foco
- **Estados de carga**: Spinners y feedback visual
- **Confirmaciones**: Modal de confirmación para eliminaciones

### Notificaciones Toast
- **Éxito**: Operaciones completadas correctamente
- **Error**: Errores de validación, red o servidor
- **Auto-close**: 5 segundos por defecto
- **Accesibilidad**: `aria-live` y `role="alert"`

### Manejo de Errores
- **Validación**: Errores por campo con descripción clara
- **HTTP**: Mapeo de códigos de error a mensajes comprensibles
- **Reintentos**: Lógica de reconexión para WebSocket
- **Fallbacks**: Estados de error graceful sin romper la UI

## 🔧 Decisiones de Diseño

### Arquitectura de Estado
- **Redux Toolkit** para gestión de estado predecible
- **Slices separados** para tareas e integración
- **Async thunks** para operaciones asíncronas con manejo de errores
- **Transformación de datos** en la capa de servicios

### Componentes
- **Composición** sobre herencia
- **Props explícitas** con JSDoc para documentación
- **Separación de responsabilidades** entre presentación y lógica
- **Reutilización** de componentes (Toast, StatusBadge)

### Validación
- **Sin dependencias externas** para mantener bundle pequeño
- **Validación en tiempo real** con feedback inmediato
- **Mensajes en español** orientados al usuario final
- **Accesibilidad** con `aria-describedby` y gestión de foco

### Networking
- **Interceptores Axios** para manejo centralizado de errores
- **Transformación bidireccional** entre formatos frontend/backend
- **WebSocket resiliente** con reconexión automática
- **Timeouts configurables** para prevenir requests colgados

## 🧪 Criterios de Aceptación

✅ **Carga inicial**: Muestra integración y lista de tareas  
✅ **Edición inline**: Modificar tareas y persistir cambios  
✅ **Toggle completar**: Cambiar estado con feedback  
✅ **Eliminación**: Confirmación y eliminación segura  
✅ **Crear tarea**: Validación, creación y redirección  
✅ **Tiempo real**: Sincronización automática vía WebSocket  
✅ **Responsivo**: Funciona en desktop y mobile  
✅ **Accesible**: Cumple estándares básicos de accesibilidad

## 🚀 Inicio Rápido

### Con Backend Real
```bash
# 1. Configurar entorno
cp .env.example .env
# Editar .env con URLs del backend

# 2. Instalar e iniciar
npm install
npm run dev
```

### Con Mocks (Sin Backend)
```bash
# 1. Instalar dependencias
npm install

# 2. Usar configuración de desarrollo (mocks automáticos)
npm run dev
# O configurar manualmente: VITE_USE_MOCKS=true
```

### Ejecutar Pruebas
```bash
# Instalar browsers de Playwright
npx playwright install

# Ejecutar pruebas principales
npm run test:e2e

# Ver resultados
npx playwright show-report
```

## 🎯 Estado de Validación

✅ **Build**: Compila sin errores  
✅ **Mocks**: Sistema funcionando con datos realistas  
✅ **E2E Tests**: Funcionalidad core validada  
✅ **Responsive**: Móvil y desktop funcional  
✅ **Accesibilidad**: Navegación por teclado y ARIA labels  
✅ **Formularios**: Validación en tiempo real implementada