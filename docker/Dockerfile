FROM openjdk:17-alpine
CMD ["./gradlew", "clean", "bootJar"]

EXPOSE 8089

COPY ./build/libs/*.jar checkpoint.jar

ENTRYPOINT ["java", "-jar", "checkpoint.jar"]
