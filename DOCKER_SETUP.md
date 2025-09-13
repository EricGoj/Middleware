# 🐳 Docker Setup - Resumen Completo

## 📋 Archivos Creados

### Configuración Docker Principal
1. **`docker-compose.yml`** - Orquestación de todos los servicios
2. **`.env.example`** - Template de variables de entorno
3. **`database/init.sql`** - Script de inicialización de PostgreSQL

### Dockerfiles
4. **`backend-middleware/Dockerfile`** - Imagen Spring Boot con Java 21
5. **`frontend-middleware/Dockerfile`** - Imagen React multi-stage con Nginx
6. **`frontend-middleware/nginx.conf`** - Configuración optimizada de Nginx

### Archivos de Optimización
7. **`.dockerignore`** - Exclusiones globales
8. **`frontend-middleware/.dockerignore`** - Exclusiones específicas del frontend

### Scripts y Herramientas
9. **`docker-test.sh`** - Script de validación de configuración
10. **`README.md`** - Actualizado con instrucciones completas

## 🏗️ Arquitectura de Servicios

```yaml
Services:
├── postgres (PostgreSQL 15)
│   ├── Port: 5432
│   ├── Database: middleware_db  
│   ├── User: middleware_user
│   └── Volume: postgres_data
│
├── backend (Spring Boot + Java 21)
│   ├── Port: 8080
│   ├── Depends: postgres
│   ├── Health: /actuator/health
│   └── Volume: backend_logs
│
├── frontend (React + Nginx)
│   ├── Port: 3000 (mapped to 80)
│   ├── Depends: backend
│   ├── Health: /health
│   └── Optimized: gzip, caching, security headers
│
└── pgadmin (Optional)
    ├── Port: 5050
    ├── Profile: tools
    └── Volume: pgadmin_data
```

## 🚀 Comandos Esenciales

### Inicio Rápido
```bash
# Copiar variables de entorno
cp .env.example .env

# Editar .env con credenciales JIRA
nano .env

# Validar configuración (opcional)
./docker-test.sh

# Levantar toda la aplicación
docker compose up --build -d

# Ver logs en tiempo real
docker compose logs -f
```

### Gestión de Servicios
```bash
# Estado de contenedores
docker compose ps

# Parar aplicación
docker compose down

# Resetear base de datos
docker compose down -v

# Reconstruir un servicio específico
docker compose build backend
docker compose up -d backend

# Acceso a contenedores
docker compose exec backend bash
docker compose exec postgres psql -U middleware_user -d middleware_db
```

## 🔧 Variables de Entorno Requeridas

### Obligatorias (JIRA)
```bash
JIRA_URL=https://tu-dominio.atlassian.net
JIRA_USERNAME=tu-email@dominio.com
JIRA_API_TOKEN=tu-api-token-secreto
```

### Opcionales (Defaults configurados)
```bash
POSTGRES_DB=middleware_db
POSTGRES_USER=middleware_user
POSTGRES_PASSWORD=middleware_password
REACT_APP_API_BASE_URL=http://localhost:8080/api
```

## 🏥 Health Checks Configurados

### Automáticos (Docker)
- **PostgreSQL**: `pg_isready` cada 30s
- **Backend**: `curl /actuator/health` cada 30s, espera 60s
- **Frontend**: `wget /health` cada 30s, espera 10s

### Manuales (Testing)
```bash
curl http://localhost:8080/actuator/health
curl http://localhost:3000/health
docker compose exec postgres pg_isready -U middleware_user
```

## 📊 Características de Producción

### Seguridad
- **Usuarios no root** en todos los contenedores
- **Security headers** en Nginx
- **Secrets** manejados vía variables de entorno
- **Network isolation** con subnet dedicada

### Performance
- **Multi-stage builds** para optimización de imágenes
- **Gzip compression** habilitado
- **Static asset caching** configurado
- **Resource limits** y health checks

### Observabilidad
- **Structured logging** con timestamps
- **Volume persistence** para logs
- **Spring Actuator** endpoints habilitados
- **Container monitoring** con Docker stats

## 🔍 Troubleshooting Común

### Backend no inicia
```bash
# Revisar logs
docker compose logs backend

# Problemas típicos:
# 1. Variables JIRA no configuradas → Verificar .env
# 2. PostgreSQL no disponible → docker compose restart postgres
# 3. Puerto ocupado → lsof -i :8080
```

### Frontend no carga
```bash
# Verificar backend
curl http://localhost:8080/actuator/health

# Reconstruir si es necesario
docker compose build frontend
docker compose up -d frontend
```

### Base de datos
```bash
# Conexión directa
docker compose exec postgres psql -U middleware_user -d middleware_db

# Reset completo
docker compose down -v
docker compose up --build
```

## 🎯 URLs de Acceso

- **Aplicación Frontend**: http://localhost:3000
- **API Backend**: http://localhost:8080
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **Spring Actuator**: http://localhost:8080/actuator
- **Base de datos**: localhost:5432
- **pgAdmin**: http://localhost:5050 (con `--profile tools`)

## 📦 Deploy en Producción

### Build para Registry
```bash
docker build -t tu-usuario/middleware-backend:latest ./backend-middleware
docker build -t tu-usuario/middleware-frontend:latest ./frontend-middleware

docker push tu-usuario/middleware-backend:latest
docker push tu-usuario/middleware-frontend:latest
```

### Variables de Producción
```bash
SPRING_PROFILES_ACTIVE=production
POSTGRES_PASSWORD=password-seguro-produccion
JIRA_URL=https://production-domain.atlassian.net
```

---

## ✅ Validación Completa

El script `docker-test.sh` valida:
- ✅ Docker y Docker Compose instalados
- ✅ Archivos de configuración presentes
- ✅ docker-compose.yml válido
- ✅ Puertos disponibles
- ✅ Estructura de archivos correcta
- ✅ Artefactos de build existentes

**¡Configuración Docker lista para producción! 🚀**