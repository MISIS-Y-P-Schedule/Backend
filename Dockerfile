# First stage: complete build environment
FROM gradle:8.4.0-jdk17-alpine AS builder
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle build --no-daemon

#  Second stage: minimal runtime environment
FROM amazoncorretto:17-alpine-jdk
EXPOSE 8080
EXPOSE 8888
RUN mkdir /app
COPY --from=builder /home/gradle/src/build/libs/*.jar /app/spring-boot-application.jar
ENTRYPOINT ["java", "-jar", "/app/spring-boot-application.jar"]