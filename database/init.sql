-- Inicialización de base de datos para Middleware
-- Este script se ejecuta automáticamente cuando se crea el contenedor de PostgreSQL

-- Crear extensiones necesarias
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Crear esquema para la aplicación
CREATE SCHEMA IF NOT EXISTS middleware;

-- Establecer búsqueda de esquemas por defecto
SET search_path TO middleware, public;

-- Crear tablas base (Spring Boot/Hibernate creará las tablas automáticamente)
-- Este archivo puede usarse para datos de inicialización si es necesario

-- Insertar datos de ejemplo (opcional)
-- INSERT INTO middleware.tasks (id, title, description, status) VALUES 
-- (uuid_generate_v4(), 'Tarea de ejemplo', 'Descripción de tarea de ejemplo', 'PENDING');

-- Crear índices adicionales si son necesarios
-- CREATE INDEX IF NOT EXISTS idx_tasks_status ON middleware.tasks(status);
-- CREATE INDEX IF NOT EXISTS idx_tasks_created_date ON middleware.tasks(created_date);

-- Configurar permisos
GRANT ALL PRIVILEGES ON SCHEMA middleware TO middleware_user;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA middleware TO middleware_user;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA middleware TO middleware_user;

-- Mensaje de confirmación
SELECT 'Base de datos inicializada correctamente' as status;