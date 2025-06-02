# Usa una imagen JDK como base
FROM eclipse-temurin:21-jdk-alpine as build

# Directorio de trabajo
WORKDIR /app

# Copiar archivos de build
COPY . .

# Construye el jar con Gradle
RUN ./gradlew clean bootJar

# Imagen final minimalista
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Copiar el jar desde la fase anterior
COPY --from=build /app/build/libs/*.jar app.jar

# Exponer el puerto
EXPOSE 8080

# Ejecutar la app
ENTRYPOINT ["java", "-jar", "app.jar"]
