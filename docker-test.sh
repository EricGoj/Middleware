#!/bin/bash

# Script de prueba para configuración Docker
# Autor: Eric Quevedo

set -e

echo "🐳 Testing Docker Setup for Middleware Application"
echo "=================================================="

# Colores para output
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m' # No Color

# Función para logging
log_info() {
    echo -e "${GREEN}[INFO]${NC} $1"
}

log_warn() {
    echo -e "${YELLOW}[WARN]${NC} $1"
}

log_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# 1. Verificar prerrequisitos
log_info "Checking prerequisites..."

if ! command -v docker &> /dev/null; then
    log_error "Docker is not installed"
    exit 1
fi

if ! docker compose version &> /dev/null; then
    log_error "Docker Compose is not installed"
    exit 1
fi

log_info "✅ Docker and Docker Compose are available"

# 2. Verificar archivos necesarios
log_info "Checking required files..."

required_files=(
    "docker-compose.yml"
    "backend-middleware/Dockerfile" 
    "frontend-middleware/Dockerfile"
    "frontend-middleware/nginx.conf"
    "database/init.sql"
    ".env.example"
)

for file in "${required_files[@]}"; do
    if [[ ! -f "$file" ]]; then
        log_error "Required file missing: $file"
        exit 1
    fi
done

log_info "✅ All required files present"

# 3. Verificar configuración de docker-compose
log_info "Validating docker-compose configuration..."

if ! docker compose config --quiet; then
    log_error "docker-compose.yml configuration is invalid"
    exit 1
fi

log_info "✅ docker-compose.yml is valid"

# 4. Verificar archivo .env
if [[ ! -f ".env" ]]; then
    log_warn "⚠️  .env file not found. Creating from example..."
    cp .env.example .env
    log_warn "📝 Please edit .env with your JIRA credentials before running docker compose up"
fi

# 5. Verificar que los puertos no estén ocupados
log_info "Checking if ports are available..."

ports=(3000 8080 5432 5050)
for port in "${ports[@]}"; do
    if lsof -Pi :$port -sTCP:LISTEN -t >/dev/null 2>&1; then
        log_warn "⚠️  Port $port is already in use"
    fi
done

# 6. Verificar si ya existen contenedores
log_info "Checking existing containers..."

if docker compose ps | grep -q "middleware"; then
    log_warn "⚠️  Some middleware containers are already running"
    log_info "Run 'docker compose down' to stop them first"
fi

# 7. Test básico de construcción (sin ejecutar)
log_info "Testing Docker build context..."

# Verificar que los JAR del backend existen
if [[ ! -f "backend-middleware/bootstrap/target/middleware-bootstrap-0.0.1-SNAPSHOT.jar" ]]; then
    log_warn "⚠️  Backend JAR not found. Run 'mvn clean package' first"
    log_info "Or use 'docker compose up --build' to build everything"
fi

# Verificar que el build del frontend existe
if [[ ! -d "frontend-middleware/dist" ]]; then
    log_warn "⚠️  Frontend dist folder not found. It will be built during Docker build"
fi

log_info "✅ Docker configuration test completed!"

echo ""
log_info "🚀 Ready to start! Run the following commands:"
echo ""
echo "  # Start all services:"
echo "  docker compose up --build -d"
echo ""
echo "  # View logs:"
echo "  docker compose logs -f"
echo ""
echo "  # Stop services:"
echo "  docker compose down"
echo ""
echo "  # Access applications:"
echo "  Frontend: http://localhost:3000"
echo "  Backend:  http://localhost:8080"
echo "  pgAdmin:  http://localhost:5050"

# 8. Mostrar health checks disponibles
echo ""
log_info "🏥 Health check endpoints:"
echo "  curl http://localhost:8080/actuator/health"
echo "  curl http://localhost:3000/health"

echo ""
echo "🎉 Configuration looks good! You're ready to go!"