# [예쁜 손 글씨 홍보 사이트](http://www.beautifulwriting.site) 입니다.

### 사이트 바로가기 -> [www.beautifulwriting.site](http://www.beautifulwriting.site)

<br>
예쁜글씨, 캘리그라피, 폼아트, 토탈공예를 활용한 작품를 소개하기위한 홍보사이트를 위해 재작한 
BACK-END APPLICATION 입니다.
<br>
<br>

### **주의 사항**

- 웹 사이트 페이지 구성을 위한 FRONT 프로젝트는 다음을 참고해주세요.
    - FRONT-END 프로젝트 [LINK](https://github.com/yudonggeun/handwriting-promotion)
- 사용 목적에 맞게 주제를 변경해서 사용하는 것 역시 가능하지만 사용시 `youdong98@naver.com`로 사용 여부를 알려주세요.

---

## Stacks

### Environment

<div style="display: flex;">
    <img style="margin-right: 5px;" src="https://img.shields.io/badge/intellij-gray?style=for-the-badge&logo=INTELLIJ IDEA&logoColor=white">
    <img style="margin-right: 5px;" src="https://img.shields.io/badge/git-F05032?style=for-the-badge&logo=git&logoColor=white">
</div>

### Development

<div style="display: flex;">
    <img style="margin-right: 5px;" src="https://img.shields.io/badge/Spring-6DB33F?style=for-the-badge&logo=Spring&logoColor=white">
    <img style="margin-right: 5px;" src="https://img.shields.io/badge/java-6DB39F?style=for-the-badge&logo=java&logoColor=#61DAFB">
</div>

### Deploy

<div style="display: flex;">
    <img style="margin-right: 5px;" src="https://img.shields.io/badge/github-181717?style=for-the-badge&logo=github&logoColor=white">
    <img style="margin-right: 5px;" src="https://img.shields.io/badge/docker-2496ED?style=for-the-badge&logo=docker&logoColor=white">
    <img style="margin-right: 5px;" src="https://img.shields.io/badge/amazon aws-232F3E?style=for-the-badge&logo=amazonaws&logoColor=white">
    <img style="margin-right: 5px;" src="https://img.shields.io/badge/linux-FCC624?style=for-the-badge&logo=linux&logoColor=black">
</div>


---

## 시작 가이드

도커 컨테이너 환경에서 실행하기 위한 **Dockerfile**이 포함된 프로젝트입니다. 따라서 도커와 함께 실행하기를 권장합니다.

0. 사전 준비
    1. Docker 설치
    2. java 설치
    3. 환경 변수 선언 스크립트 작성

1. 앱 실행하기

- 실행 예제

```bash
$chmod +x gradlew
$cd ./build/libs
$. ./secrets.sh
$java -jar example-build-file-name.jar 1> app.log 2> error.log
```
- 도커를 이용한 배포
```bash
$bash ./script/start.sh
```
스크립트를 이용하여 배포를 시도하면 사전 준비가 필요합니다.
1. 도커 설치
2. 도커 네트워크 `kong-net` 만들기
3. `start.sh` 환경변수를 배포 환경에 맞게 수정하기
    * 파일 디렉토리 구조에 대한 환경변수를 설정하는 작업입니다.
4. 생성한 환경변수 선언 스크립트 `screts.sh`를 도커 볼륨으로 지정할 디렉토리에 저장하기
    * spring boot 실행에 필요한 환경변수를 설정하는 스크립트입니다.

```shell
echo "$(date +%c) : set secrets environment"
# environment 운영환경 deploy 로컬(개발) 환경 local
export ENVIRONMENT_TYPE=deploy
# database secrets
export DATABASE_URL=127.0.0.1
export DATABASE_USERNAME=example_username
export DATABASE_PASSWORD=example_password
# jwt secrets
export JWT_SECRET=123456789...
```
`start.sh`를 통하여 배포한 경우<br> 코드 수정사항 적용한 새로운 jar 배포를 `start.sh` 재실행을 통해서 
손쉽게 재배포가 가능합니다.

해당 프로젝트는 `GIT ACTION`을 이용하여 자동 배포 시스템을 구축하고 있기에 이를 위한 스크립트 파일이 존재합니다. `script/start.sh`의 `PROJECT_ROOT`의 경로를 배포 환경에 맞게
소스 디렉터리로 수정하고 실행하면 위의 실행과정을 스크립트로 한번에 실행할 수 있습니다.

---
## 구현 기능
* 홍보글 CRUD
* 이미지 파일 관리
* JWT를 이용한 사용자 로그인
* 이미지 압축
* oath 로그인(구글, 카카오)

## 개발 중
* 홍보 페이지 image 페이징
---

## 아키텍처

* 배포 파이프라인

![배포 구조](introduceeploy.PNG)

* 서비스 구조

![서비스 구조](introducerchitecture.PNG)