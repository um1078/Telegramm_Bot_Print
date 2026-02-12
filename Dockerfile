FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY target/telegram-bot-helper.jar app.jar
ENTRYPOINT ["java","-jar","app.jar"]
