FROM openjdk
CMD ["./mvnw", "clean", "package"]
COPY ./build/libs/handwriting-0.0.1-SNAPSHOT.jar /spring-webapp.jar
EXPOSE 8080
ENTRYPOINT java -jar /spring-webapp.jar

