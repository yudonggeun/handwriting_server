FROM adoptopenjdk/openjdk11
CMD ["./mvnw", "clean", "package"]

RUN mkdir /app
#ADD ./build/libs .
EXPOSE 8080
WORKDIR /app
ENTRYPOINT ["java", "-jar", "/app/spring-webapp.jar", ">", "/app/log/application.log", "2>", "/app/log/error.log"]