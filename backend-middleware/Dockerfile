# Dockerfile para Backend (Spring Boot)
FROM openjdk:21-jdk-slim

# Información del maintainer
LABEL maintainer="Eric Quevedo"
LABEL description="Middleware Backend - Spring Boot Application"

# Variables de entorno
ENV JAVA_OPTS="-Xms512m -Xmx1024m"
ENV SPRING_PROFILES_ACTIVE=docker

# Crear directorio de trabajo
WORKDIR /app

# Crear usuario no root para seguridad
RUN groupadd -r appuser && useradd -r -g appuser appuser

# Copiar el JAR ejecutable
COPY bootstrap/target/middleware-bootstrap-*.jar app.jar

# Cambiar propietario del directorio
RUN chown -R appuser:appuser /app

# Cambiar a usuario no root
USER appuser

# Exponer puerto
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1

# Comando para ejecutar la aplicación
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]