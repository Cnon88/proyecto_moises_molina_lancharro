# Multi-stage build para optimizar el tamaño de la imagen

# Fase 1: Build y Test
FROM eclipse-temurin:17-jdk-jammy AS build-and-test

WORKDIR /app

# Copiar archivos de configuración de Gradle (Kotlin DSL)
COPY build.gradle.kts settings.gradle.kts gradlew ./
COPY gradle gradle

# Dar permisos de ejecución a gradlew
RUN chmod +x gradlew

# Descargar dependencias (esto se cachea si no cambian los archivos .kts)
RUN ./gradlew dependencies --no-daemon

# Copiar código fuente
COPY src src

# Ejecutar tests y build en una sola fase
# RUN ./gradlew clean test bootJar --no-daemon --full-stacktrace
RUN ./gradlew clean build -x test bootJar --no-daemon --full-stacktrace

# Fase 2: Runtime - imagen final optimizada
FROM eclipse-temurin:17-jre-jammy

# Crear usuario no-root para seguridad
RUN groupadd -r spring && useradd -r -g spring spring

WORKDIR /app

# Copiar solo el JAR desde la fase de build
COPY --from=build-and-test /app/build/libs/*.jar app.jar

# Cambiar propietario del archivo
RUN chown spring:spring app.jar

# Cambiar a usuario no-root
USER spring

# Exponer el puerto 8080
EXPOSE 8080

# Variables de entorno JVM recomendadas
ENV JAVA_OPTS="-Xmx512m -Xms256m -Djava.security.egd=file:/dev/./urandom"

# Comando para ejecutar la aplicación
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
