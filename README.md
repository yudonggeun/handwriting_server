# Beautiful Handwriting Promotion Site
This is a BACK-END APPLICATION created for a promotional website to showcase works utilizing beautiful handwriting, calligraphy, foam art, and total crafts.

> For the FRONT project for web site page composition, please refer to this [link](https://github.com/yudonggeun/handwriting-promotion).

## Stacks

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

## Getting Started

* To run
```bash
docker compose up
```
* Access address : The published compose file is configured with local execution in mind.
```
http://localhost
```
* Initial admin user
```
id=admin
password=1234
```
## Implemented Features

* CRUD operations for promotional posts
* Image file management
* User login using JWT
* Image compression
* OAuth login (Google, Kakao)

## Architecture
### Deployment Pipeline
![배포 구조](introduce/deploy.PNG)


### Service Structure

![서비스 구조](introduce/architecture.PNG)
