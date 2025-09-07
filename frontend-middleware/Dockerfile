# Multi-stage Dockerfile para Frontend (React + TypeScript)

# Etapa 1: Build
FROM node:18-alpine as build

# Información del maintainer
LABEL maintainer="Eric Quevedo"
LABEL description="Middleware Frontend - React Application"

# Crear directorio de trabajo
WORKDIR /app

# Copiar archivos de dependencias
COPY package*.json ./

# Instalar dependencias
RUN npm ci --only=production

# Copiar código fuente
COPY . .

# Construir la aplicación
RUN npm run build

# Etapa 2: Production
FROM nginx:alpine

# Copiar configuración personalizada de Nginx
COPY nginx.conf /etc/nginx/nginx.conf

# Copiar archivos estáticos desde la etapa de build
COPY --from=build /app/dist /usr/share/nginx/html

# Crear usuario no root para seguridad
RUN addgroup -g 1001 -S appuser && \
    adduser -S appuser -u 1001

# Exponer puerto
EXPOSE 80

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=10s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost/ || exit 1

# Ejecutar Nginx
CMD ["nginx", "-g", "daemon off;"]