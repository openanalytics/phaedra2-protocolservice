FROM openjdk:16-jdk-slim-buster

ARG JAR_FILE
ADD $JAR_FILE /opt/phaedra/protocolservice.jar

ENV USER phaedra
RUN useradd -c 'phaedra user' -m -d /home/$USER -s /bin/nologin $USER
WORKDIR /opt/phaedra
USER $USER

CMD ["java", "-jar", "/opt/phaedra/protocolservice.jar", "--spring.jmx.enabled=false"]
