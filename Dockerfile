FROM adoptopenjdk/openjdk11
CMD ["./mvnw", "clean", "package"]

RUN mkdir /app
EXPOSE 8080, 443
WORKDIR /app

# secrets 환경 변수 설정, 배포전 환경 변수 설정 셀을 생성해야한다.
ENTRYPOINT [".", "/app/secrets.sh", ">>", "/app/secrets.log"]
ENTRYPOINT ["java", "-jar", "/app/spring-webapp.jar", "1>", "/app/log/application.log", "2>", "/app/log/error.log"]