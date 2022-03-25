# ✅ 소개


- 일반적인 중고 거래 서비스에 "경매" 기능을 추가한 중고 거래 플랫폼 API 서버입니다.
- 대용량 트래픽 처리를 고려해 개발하였으며 테스트 코드 작성, CI/CD 자동화 등을 통해 프로젝트의 완성도를 높였습니다.
    
<br>

# ✅ 서버 구성도

<p align="center"><img src="https://s3.us-west-2.amazonaws.com/secure.notion-static.com/76fcbfd6-9834-42b3-8b95-fab26adec8fb/Untitled.png?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Content-Sha256=UNSIGNED-PAYLOAD&X-Amz-Credential=AKIAT73L2G45EIPT3X45%2F20220325%2Fus-west-2%2Fs3%2Faws4_request&X-Amz-Date=20220325T032855Z&X-Amz-Expires=86400&X-Amz-Signature=442f7b648dca49633f49c0f0c91b5b193c72abeea9e9c2d49311822245b62119&X-Amz-SignedHeaders=host&response-content-disposition=filename%20%3D%22Untitled.png%22&x-id=GetObject" width="800" height="500"/></p>
<p align="center"><img src="https://s3.us-west-2.amazonaws.com/secure.notion-static.com/f1f23c89-b0f2-4256-a1b8-92e40997fee7/Untitled.png?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Content-Sha256=UNSIGNED-PAYLOAD&X-Amz-Credential=AKIAT73L2G45EIPT3X45%2F20220325%2Fus-west-2%2Fs3%2Faws4_request&X-Amz-Date=20220325T032908Z&X-Amz-Expires=86400&X-Amz-Signature=f7896da093b09e67a869db2e2c0e63f17ed0fcc141d703c9056b24e5fb47b279&X-Amz-SignedHeaders=host&response-content-disposition=filename%20%3D%22Untitled.png%22&x-id=GetObject" width="700" height="400"/></p>

<br>

# ✅ 사용 기술

- Java 11
- Spring Boot + Gradle
- Hibernate/SpringJPA/QueryDSL
- Docker
- Jenkins
- Flyway
- Mockito
- AWS

<br>

# ✅ CI/CD

<p align="center"><img src="https://s3.us-west-2.amazonaws.com/secure.notion-static.com/1831e0dc-f21c-4d97-9f37-55eb0b6250f0/Untitled.png?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Content-Sha256=UNSIGNED-PAYLOAD&X-Amz-Credential=AKIAT73L2G45EIPT3X45%2F20220325%2Fus-west-2%2Fs3%2Faws4_request&X-Amz-Date=20220325T033331Z&X-Amz-Expires=86400&X-Amz-Signature=c35c00194911e60cbfcc9c4efd6f1de30c6c0e2f7e4aaaf3df408c7dadf4cb9a&X-Amz-SignedHeaders=host&response-content-disposition=filename%20%3D%22Untitled.png%22&x-id=GetObject" width="500" height="350"/></p>

1. 코드 PR 후 develop 브랜치에 코드가 merge 되면 Webhook으로 Jenkins에 알린다.
2. Jenkins는 SpringBoot를 테스트/빌드 후  jar 파일을 만든다. 
3. Dockerfile로 jar 파일이 포함된 DockerImage를 만들어 ECR에 업로드한다.
4. ECR에 업로드된 이미지로 ECS에 컨테이너를 새로 배포한다.
5. 2~4 과정 중 실패하면 개발자에게 Email을 보내 알린다.

<br>

# ✅ 주요 기능

- RDBMS row Lock으로 동시성 문제를 해결한 입찰 시스템 
- 실시간 경매 가격을 위한 SSE와 Redis Pub/Sub
- 경매 결과 안내를 위해 Lambda에서 SMS 발송
- Dockerfile을 이용한 이미지 생성
- Jenkins를 이용한 CI/CD 자동화
- AWS ECS 무중단 배포
- 인증/인가를 위한 JWT
- 반복되는 로직 분리를 위한 AOP

<br>

# ✅ 관련 문서

**📌  [입찰 시스템 어떻게 구축할 것인가?](https://www.notion.so/04bea354949b4c28944c5d942d4901c0)** 

**📌  [최종 입찰자에게 SMS 발송](https://www.notion.so/SMS-16d529c7439649479c9793f0613bbab4)**

**📌  [실시간 경매 상품 가격 받기](https://www.notion.so/e6e7a3105f7440869daa1f4e35850355)**

**📌**  [CI/CD 자동화와 무중단 배포](https://www.notion.so/CI-CD-c61f3f70bccf4348b78cd08a3b683602)
