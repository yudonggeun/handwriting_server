#!/usr/bin/env bash

PROJECT_ROOT="/home/ubuntu/spring"
PROJECT_SOURCE_DIRECTORY_PATH="/home/ubuntu/spring/handwriting_server";

JAR_FILE="$PROJECT_ROOT/application/spring-webapp.jar"

APP_LOG="$PROJECT_ROOT/application/log/application.log"
ERROR_LOG="$PROJECT_ROOT/application/log/error.log"
DEPLOY_LOG="$PROJECT_ROOT/deploy.log"

TIME_NOW=$(date +%c)

# build 파일 복사
echo "$TIME_NOW : $JAR_FILE 파일 복사" : $DEPLOY_LOG
cp $PROJECT_SOURCE_DIRECTORY_PATH/build/libs/handwriting-0.0.1-SNAPSHOT.jar $JAR_FILE

# 도커 컨테이너 새로운 빌드.jar 로 재실행
echo "$TIME_NOW : $JAR_FILE 도커 재실행" : $DEPLOY_LOG
sudo docker restart $(sudo docker ps -aqf name=handwriting_spring)