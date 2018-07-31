FROM openjdk:8-jre-slim
COPY build/libs/miStayMicroService-0.1.0.jar /
WORKDIR /
CMD ["java", "-jar", "miStayMicroService-0.1.0.jar"]
