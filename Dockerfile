# 1단계: 빌드
FROM eclipse-temurin:17-jdk AS build
WORKDIR /app
COPY gradlew ./
COPY gradle ./gradle
COPY build.gradle* settings.gradle* ./
COPY src ./src
RUN chmod +x ./gradlew
RUN ./gradlew --no-daemon clean bootJar

# 2단계: 런타임
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/build/libs/*SNAPSHOT.jar app.jar
EXPOSE 8080

ENV SPRING_PROFILES_ACTIVE=prod

ENTRYPOINT ["java", "-jar", "app.jar"]