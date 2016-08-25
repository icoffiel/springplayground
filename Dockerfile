FROM frolvlad/alpine-oraclejdk8:slim
MAINTAINER Iain

# Create working directories
VOLUME /tmp

# Copy overt the app
ADD target/spring-playground-0.0.1-SNAPSHOT.jar app.jar

# Add a modification time
RUN sh -c 'touch /app.jar'

# Runt he app and create a source of entropy
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","app.jar"]