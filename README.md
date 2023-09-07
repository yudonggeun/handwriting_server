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

0. 사전 준비
    * docker-compose 설치
1. 앱 실행하기

- 실행 예제
docker-compose.yml 존재하는 디렉토리에서 다음 명령어 실행
```bash
docker compose up
```
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