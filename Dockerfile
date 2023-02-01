FROM adoptopenjdk/openjdk11
CMD ["./mvnw", "clean", "package"]

RUN mkdir /app
WORKDIR /app
#ADD ./build/libs .
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "handwriting-0.0.1-SNAPSHOT.jar"]