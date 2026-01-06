FROM eclipse-temurin:21-jdk
MAINTAINER william.com
COPY target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]