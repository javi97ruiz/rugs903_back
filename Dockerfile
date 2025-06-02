# Usa imagen oficial de OpenJDK con Gradle
FROM gradle:8.5-jdk21 AS builder

# Copia el proyecto
COPY --chown=gradle:gradle . /app
WORKDIR /app

# Compila el proyecto
RUN gradle build -x test

# Nueva imagen con solo JDK para correr el jar
FROM eclipse-temurin:21-jdk

WORKDIR /app

# Copia el jar generado
COPY --from=builder /app/build/libs/*.jar app.jar

# Puerto que usará Render (Render expone por defecto el 10000)
EXPOSE 8080

# Ejecuta el backend
ENTRYPOINT ["java", "-jar", "app.jar"]
