FROM openjdk:8-jre-slim
COPY build/libs/mistayMicroService-0.1.0.jar /
WORKDIR /
CMD ["java", "-jar", "mistayMicroService-0.1.0.jar"]
