FROM eclipse-temurin:17-jre
WORKDIR /opt/app
COPY target/*.jar examregistration.jar
CMD ["java", "-jar", "examregistration.jar"]