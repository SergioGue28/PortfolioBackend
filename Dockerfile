# Etapa de construcción
FROM maven:3.9.5-eclipse-temurin-17 AS build

WORKDIR /app

# Copiar todos los archivos del proyecto
COPY . .

# Compilar y empaquetar el proyecto (saltando tests)
RUN mvn clean package -DskipTests

# Etapa final
FROM eclipse-temurin:17-jdk

WORKDIR /app

# Copiar el .jar desde la etapa de build
COPY --from=build /app/target/backendportfolio-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]
