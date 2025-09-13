# Middleware Challenge

## 📋 Descripción
Mini Aplicacion para Gestionar Issues, con un frontend muy simple para poder gestionar tareas, comunicacion en tiempo real sobre actualizaciones sobre el gestor de tareas que este integrado, en este caso es JIRA. Como backend la idea es hacer una API middleware que funcione como comunicacion entre el interfaz y JIRA, pero implementado arquitectura hexagonal, para que facilmente se pueda integrar con otro servicio de gestion de Tareas, como MIRO.

## 🏗️ Arquitectura

### Arquitectura Hexagonal (Ports & Adapters)
**Decisión:** Implementación estricta de arquitectura hexagonal.

**Porque elegi esta arquitectura?** 
- **Aislamiento del dominio**: La lógica de negocio permanece independiente de frameworks y servicios externos
- **Testabilidad**: Facilita el testing unitario mediante inyección de dependencias y mocks de puertos
- **Flexibilidad**: Permite cambiar adaptadores (ej: Jira por otro sistema) sin modificar el dominio
- **Mantenibilidad**: Separación clara de responsabilidades facilita la evolución del código

## 🔧 Stack Tecnológico

### Backend

| Tecnología | Versión | Justificación |
|------------|---------|---------------|
| **Java** | 21 | LTS con características modernas (records, pattern matching) |
| **Spring Boot** | 3.5.x | Framework maduro con excelente soporte para arquitectura hexagonal vía inyección de dependencias |
| **PostgreSQL** | 15 | Base de datos relacional para garantizar consistencia ACID en sincronización bidireccional |
| **JPA/Hibernate** | - | Abstracción de persistencia que se alinea con el patrón Repository |
| **Maven** | 3.9.x | Gestión de dependencias estandarizada en el ecosistema Java empresarial |

### Frontend

| Tecnología | Versión | Justificación |
|------------|---------|---------------|
| **React** | 18.x | Framework reactivo para UI dinámica con excelente ecosistema |
| **Redux Toolkit** | 2.x | Gestión de estado predecible con DevTools para debugging |
| **RTK Query** | - | Caching y sincronización de datos con el backend |
| **TypeScript** | 5.x | Type safety para reducir errores en runtime |

### Arquitectura de la aplicacion

![Aquitectura](arquitecturabase.png)

## 💡 Decisiones de Diseño


## Como creamos una nueva Issue en base y en JIRA?
![WorkflowNewIssue](workflowCreaTarea.png)


### PostgreSQL vs NoSQL
**Elección:** PostgreSQL

**Razones:**
- **Estructura consistente**: Las tareas tienen esquema fijo y relaciones bien definidas
- **Transaccionalidad**: Operaciones ACID críticas para mantener consistencia con Jira
- **Consultas complejas**: SQL facilita reportes y búsquedas avanzadas
- **Integridad referencial**: Constraints para garantizar consistencia de datos

### Sincronización Bidireccional
- **Webhook de Jira**: Recepción de eventos en tiempo real para mantener sincronización
- **Patrón Outbox**: Garantiza eventual consistency en caso de fallos de red

### Patrones Implementados
- **Repository Pattern**: Abstracción de la capa de persistencia
- **Use Case Pattern**: Cada operación de negocio como caso de uso independiente
- **DTO Pattern**: Separación entre modelos de dominio y representaciones externas
- **Mapper Pattern**: Traducción entre entidades de dominio y DTOs

## 🚀 Instalación y Configuración

### Prerrequisitos

**Opción 1: Con Docker (Recomendado)**
```bash
# Solamente Docker y Docker Compose
docker --version     # >= 20.0
docker compose version  # >= 2.0
```

**Opción 2: Desarrollo Local**
```bash
# Backend
java --version       # OpenJDK 21
mvn --version        # Maven 3.9.x

# Frontend  
node --version       # Node.js 18+
npm --version        # npm 8+

# Base de datos
postgresql           # PostgreSQL 15+
```

### 🐳 Ejecución con Docker (Recomendado)

#### 1. Configurar Variables de Entorno
```bash
# Copiar el archivo de ejemplo
cp .env.example .env

# Editar con tus credenciales de JIRA
nano .env
```

#### 2. Validar Configuración (Opcional)
```bash
# Ejecutar script de validación
./docker-test.sh
```

#### 3. Construir y Levantar la Aplicación
```bash
# Levantar toda la aplicación con un solo comando
docker compose up --build

# O en modo detached (en background)
docker compose up --build -d
```

#### 4. Acceder a la Aplicación
- **Frontend:** http://localhost:3000
- **Backend API:** http://localhost:8080
- **Base de datos:** localhost:5432
- **pgAdmin (opcional):** http://localhost:5050

#### 5. Comandos Útiles de Docker
```bash
# Ver logs de todos los servicios
docker compose logs -f

# Ver logs de un servicio específico
docker compose logs -f backend
docker compose logs -f frontend
docker compose logs -f postgres

# Reiniciar un servicio
docker compose restart backend

# Parar la aplicación
docker compose down

# Parar y eliminar volúmenes (resetea la base de datos)
docker compose down -v

# Reconstruir solo un servicio
docker compose build backend
docker compose up -d backend

# Ejecutar comandos dentro de un contenedor
docker compose exec backend bash
docker compose exec postgres psql -U middleware_user -d middleware_db

# Levantar pgAdmin para gestión de base de datos
docker compose --profile tools up -d
```

### 🛠️ Desarrollo Local (Alternativo)

#### Backend
```bash
cd backend-middleware

# Configurar Java 21
export JAVA_HOME=/path/to/openjdk-21
export PATH=$JAVA_HOME/bin:$PATH

# Compilar y ejecutar
mvn clean install
mvn spring-boot:run -pl bootstrap
```

#### Frontend
```bash
cd frontend-middleware

# Instalar dependencias
npm install

# Modo desarrollo
npm run dev

# Construir para producción
npm run build
```

#### Base de Datos Local
```bash
# Crear base de datos PostgreSQL
createdb middleware_db
psql middleware_db -f database/init.sql
```

### Testing

#### Backend
```bash
cd backend-middleware
mvn test

# Con Docker
docker compose exec backend mvn test
```

#### Frontend
```bash
cd frontend-middleware
npm test

# Tests E2E
npm run test:e2e

# Con Docker
docker compose exec frontend npm test
```

### 🔧 Configuración de Variables de Entorno

El archivo `.env` debe contener:

```bash
# JIRA Configuration (Obligatorio)
JIRA_URL=https://your-domain.atlassian.net
JIRA_USERNAME=your-email@domain.com
JIRA_API_TOKEN=your-api-token

# Database (Opcional - usa defaults)
POSTGRES_DB=middleware_db
POSTGRES_USER=middleware_user
POSTGRES_PASSWORD=middleware_password

# Application
REACT_APP_API_BASE_URL=http://localhost:8080/api
```

## 🔌 Integración con Jira

### 1. Obtener API Token de JIRA

1. **Ir a Atlassian Account Settings:**
   - Navegar a: https://id.atlassian.com/manage-profile/security/api-tokens
   - Click en "Create API token"
   - Dar un nombre descriptivo (ej: "Middleware App")
   - **Copiar y guardar el token** (no se puede ver después)

2. **Configurar en .env:**
   ```bash
   JIRA_URL=https://tu-dominio.atlassian.net
   JIRA_USERNAME=tu-email@dominio.com  
   JIRA_API_TOKEN=tu-api-token-aqui
   ```

### 2. Configuración del Webhook (Para sincronización en tiempo real)

#### Opción A: Desarrollo Local con ngrok
```bash
# Instalar ngrok
brew install ngrok  # macOS
# o descargar desde https://ngrok.com/

# Exponer puerto local 8080
ngrok http 8080

# Copiar la URL HTTPS generada (ej: https://abcd-123-45-67-89.ngrok.io)
```

#### Opción B: Deployment en producción
```bash
# URL de tu servidor en producción
https://tu-dominio.com/api/webhooks/jira
```

#### Configurar Webhook en JIRA:
1. **Ir a JIRA Settings:**
   - JIRA → Settings → System → Webhooks
   - Click "Create a Webhook"

2. **Configurar Webhook:**
   ```
   Name: Middleware Sync
   Status: Enabled
   URL: https://tu-url/api/webhooks/jira
   
   Events:
   ✅ Issue Created
   ✅ Issue Updated  
   ✅ Issue Deleted
   ✅ Comment Created
   ✅ Comment Updated
   ✅ Comment Deleted
   ```

3. **Filtros JQL (opcional):**
   ```sql
   project = "TU-PROYECTO"
   ```

### 3. Testing de Integración

```bash
# Probar conexión con JIRA
curl -X GET "http://localhost:8080/api/health/jira" \
  -H "Authorization: Bearer tu-token"

# Con Docker
docker compose exec backend curl http://localhost:8080/api/health/jira
```

## 🐳 Estructura Docker

```
middleware/
├── docker-compose.yml          # Orquestación de servicios
├── .env.example               # Template de variables de entorno
├── database/
│   └── init.sql              # Inicialización de PostgreSQL
├── backend-middleware/
│   ├── Dockerfile            # Imagen Spring Boot
│   └── [código fuente]
└── frontend-middleware/
    ├── Dockerfile            # Imagen React + Nginx
    ├── nginx.conf            # Configuración de Nginx
    └── [código fuente]
```

### Servicios Docker:
- **postgres:** Base de datos PostgreSQL 15
- **backend:** API Spring Boot (puerto 8080)  
- **frontend:** React SPA + Nginx (puerto 3000)
- **pgadmin:** Interfaz de gestión DB (puerto 5050, opcional)

## 🔧 Troubleshooting

### Problemas Comunes

#### Backend no inicia
```bash
# Revisar logs
docker compose logs backend

# Problemas comunes:
# - Variables JIRA no configuradas
# - PostgreSQL no disponible
# - Puerto 8080 ocupado

# Soluciones:
# Verificar .env
cat .env

# Reiniciar PostgreSQL  
docker compose restart postgres
docker compose up backend
```

#### Frontend no carga
```bash  
# Revisar logs
docker compose logs frontend

# Verificar que backend esté corriendo
curl http://localhost:8080/actuator/health

# Reconstruir imagen
docker compose build frontend
docker compose up -d frontend
```

#### Base de datos no conecta
```bash
# Probar conexión directa
docker compose exec postgres psql -U middleware_user -d middleware_db

# Resetear volúmenes
docker compose down -v
docker compose up --build
```

#### Puertos ocupados
```bash
# Encontrar proceso usando puerto
lsof -i :8080
lsof -i :3000
lsof -i :5432

# Cambiar puertos en docker-compose.yml si es necesario
```

### Logs y Debugging
```bash
# Todos los logs en tiempo real
docker compose logs -f

# Solo un servicio
docker compose logs -f backend
docker compose logs -f frontend  
docker compose logs -f postgres

# Ejecutar comandos dentro del contenedor
docker compose exec backend bash
docker compose exec frontend sh

# Ver estado de contenedores
docker compose ps

# Ver uso de recursos
docker stats
```

### Health Checks
```bash
# Backend Spring Boot
curl http://localhost:8080/actuator/health

# Frontend
curl http://localhost:3000/health

# Base de datos
docker compose exec postgres pg_isready -U middleware_user
```

## 📊 Monitoring y Logs

### Logs Persistentes
Los logs se guardan en volúmenes Docker:
- Backend: `/var/lib/docker/volumes/middleware_backend_logs`
- PostgreSQL: logs del sistema Docker

### Métricas de Aplicación
- **Spring Actuator:** http://localhost:8080/actuator
- **Endpoints disponibles:**
  - `/actuator/health` - Estado de la aplicación
  - `/actuator/metrics` - Métricas de rendimiento
  - `/actuator/info` - Información de la aplicación

## 📚 Documentación Técnica

- [Build Report](BUILD_REPORT.md) - Reporte detallado de construcción
- [API Documentation](http://localhost:8080/swagger-ui.html) - Swagger UI (cuando la app esté corriendo)
- [Arquitectura Hexagonal](arquitecturabase.png) - Diagrama de arquitectura
- [Workflow Issues](workflowCreaTarea.png) - Flujo de creación de tareas

## 🚀 Deploy en Producción

### Docker Registry
```bash
# Construir imágenes para producción
docker build -t middleware-backend:latest ./backend-middleware
docker build -t middleware-frontend:latest ./frontend-middleware

# Push a registro (ejemplo DockerHub)
docker tag middleware-backend:latest tu-usuario/middleware-backend:latest
docker push tu-usuario/middleware-backend:latest
```

### Variables de Producción
Configurar en servidor de producción:
```bash
SPRING_PROFILES_ACTIVE=production
JIRA_URL=https://tu-dominio.atlassian.net
POSTGRES_PASSWORD=password-seguro-production
```

## 👤 Autor
**Eric Quevedo**
- Arquitectura Hexagonal
- Spring Boot 3.5 + Java 21  
- React 18 + TypeScript
- Docker & PostgreSQL

---

## 🎯 Quick Start

```bash
# 1. Clonar y configurar
git clone <repo-url>
cd middleware
cp .env.example .env
# Editar .env con tus credenciales JIRA

# 2. Levantar todo
docker compose up --build -d

# 3. Verificar
open http://localhost:3000
curl http://localhost:8080/actuator/health

# 4. Ver logs
docker compose logs -f
```

**¡Listo! 🎉 La aplicación debe estar corriendo en minutos.**
