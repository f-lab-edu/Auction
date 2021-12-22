# Auction
상품 경매 서비스



QueryDSL 사용법

1) IntelliJ IDEA 사용 시

- Build.gradle에서 오른쪽에 Gradle버튼을 클릭한다.
- Service에 Tasks에 other에서 compileQuerydsl버튼을 클릭하여 Qfile을 생성한다
- 안될 시 service에서 Run Configurations에 cleanQuerydslSourceDir클릭 후 initQuerydslSourceDir을 클릭한다
- 그 이후 다시 service에 Task에 oteher에서 compileQuerydsl버튼으로 Qfile을 생성한다.

DockerFile 활용 방법

- gradle에서 Acution [bootJar]를 실행한다
- 이미지를 만든 후 태그를 473778033566.dkr.ecr.us-east-1.amazonaws.com/auction-service:latest로 만듭니다
- docker push를 통해 473778033566.dkr.ecr.us-east-1.amazonaws.com/auction-service:latest로 푸시를 합니다
- (aws cli 미 설치시 설치 필요)
- (aws configure 에서 accessKey secretKey 설정 필요 해당 키는 문의바랍니다)

Docker-Compose 사용법

- Docker-compose.yml파일이 있는 dir에서 터미널로 "docker-compose up -d" 명령으를 수행한다