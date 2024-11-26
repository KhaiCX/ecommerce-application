FROM openjdk:17-jdk-alpine
EXPOSE 8080
ADD target/ecommerce-application.jar ecommerce-application.jar
ENTRYPOINT ["java", "-jar", "ecommerce-application.jar"]