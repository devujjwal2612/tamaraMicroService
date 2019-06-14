FROM openjdk:8-jre-slim
COPY build/libs/tamaraMicroService-0.1.0.jar /
WORKDIR /
CMD ["java", "-jar", "tamaraMicroService-0.1.0.jar"]
