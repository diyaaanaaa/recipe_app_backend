FROM openjdk:11-alpine
ARG JAR_FILE=target/recipe-api-1.0.0.jar
ADD ${JAR_FILE} app.jar
CMD [ "java", "-jar", "app.jar" ]