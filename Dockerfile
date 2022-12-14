FROM adoptopenjdk/openjdk11
CMD ["./mvnw", "clean", "package"]
ADD ./build/libs .
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "handwriting-*-SNAPSHOT.jar"]