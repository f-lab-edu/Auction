# Auction
상품 경매 서비스



QueryDSL 사용법

1) IntelliJ IDEA 사용 시

- Build.gradle에서 오른쪽에 Gradle버튼을 클릭한다.
- Service에 Tasks에 other에서 compileQuerydsl버튼을 클릭하여 Qfile을 생성한다
- 안될 시 service에서 Run Configurations에 cleanQuerydslSourceDir클릭 후 initQuerydslSourceDir을 클릭한다
- 그 이후 다시 service에 Task에 oteher에서 compileQuerydsl버튼으로 Qfile을 생성한다.

DockerFile 생성 방법

- gradle에서 Acution [bootJar]를 실행한다
- Dockerfile이 위히한 곳에서 docker build -t auction-service:0.0 ./ 명령어를 입력한다
- (이미지파일 이름):(tag)
- docker tag auction-service:0.0 acecic82/auction-service:0.0을 입력하여 태그파일을 만든다
- (acecic82/auction-service는 현재 사용하고 있는 도커 허브 repository 주소)
- docker push acecic82/auction-service:0.0를 입력하여(이미지 파일 이름이 다를경우 변경될 수 있음) 해당 docker hub repo로 푸시해준다
- docker-compose 파일의 app의 image부분을 적절한 이미지파일 주소로 변경하여(주로 태그) pull 받아올 수 있게 변경한다

Docker-Compose 사용법

- Docker-compose.yml파일이 있는 dir에서 터미널로 "docker-compose up -d" 명령으를 수행한다