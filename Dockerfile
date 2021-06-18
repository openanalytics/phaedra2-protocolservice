FROM openjdk:11

ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} pha2_protocolservice.jar

ENTRYPOINT ["java", "-jar", "/pha2_protocolservice.jar"]