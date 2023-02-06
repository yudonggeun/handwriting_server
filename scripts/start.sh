#!/usr/bin/env bash

PROJECT_ROOT="/home/ubuntu/spring"
PROJECT_APPLICATION_PATH="$PROJECT_ROOT/application"
PROJECT_SOURCE_DIRECTORY_PATH="$PROJECT_ROOT/handwriting_server"

JAR_FILE="$PROJECT_APPLICATION_PATH/spring-webapp.jar"

APP_LOG="$PROJECT_APPLICATION_PATH/log/application.log"
ERROR_LOG="$PROJECT_APPLICATION_PATH/log/error.log"
DEPLOY_LOG="$PROJECT_ROOT/deploy.log"

TIME_NOW=$(date +%c)

# build 파일 복사
echo "$TIME_NOW > $JAR_FILE 파일 복사" >>$DEPLOY_LOG
cp $PROJECT_SOURCE_DIRECTORY_PATH/build/libs/handwriting-0.0.1-SNAPSHOT.jar $JAR_FILE

# 생성된 도커 컨테이너가 없다면 생성
if [ -z $(sudo docker ps -aqf name=handwriting_spring)]; then
  # 생성된 도커 이미지가 없다면 생성
  if [-z $(sudo docker images --filter=reference="handwriting-spring" -q)]; then
    echo "$TIME_NOW > 도커 이미지 생성" >>$DEPLOY_LOG
    sudo docker build -t handwriting-spring:0.2 .
  fi
  echo "$TIME_NOW > 도커 컨테이너 생성" >>$DEPLOY_LOG
  sudo docker run -d \
    --name handwriting_spring \
    -v $PROJECT_APPLICATION_PATH:/app \
    --network=kong-net \
    -p 8080:8080 \
    handwriting-spring:0.2
# 빌드 결과 반영을 위한 재실행
else
  # 도커 컨테이너 새로운 빌드.jar 로 재실행
  echo "$TIME_NOW > 도커 재실행" >>$DEPLOY_LOG
  sudo docker restart $(sudo docker ps -aqf name=handwriting_spring)
fi
