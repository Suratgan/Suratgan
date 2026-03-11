# 🍽️ Suratgan — 음식 주문관리 플랫폼
음식점 정보 제공부터 메뉴 조회, 주문 생성, 결제 처리까지 하나의 흐름으로 연결되는 End-to-End 주문 처리 시스템입니다.

## :book: Project Overview
- **음식점 관리, 메뉴 제공, 주문 생성, 결제 흐름**을 갖춘 음식 주문 서비스 제공
- 도메인 중심 설계(Domain-Driven Structure)로 확장성과 유연성을 확보
- JWT 기반 세션/인가 처리 및 보안 구조
- RESTful API 설계 원칙 준수
- 결제 흐름 구현 및 트랜잭션 관리
- Docker 기반 배포 환경 구성

## :dart: Project Goals
- 음식점 및 메뉴 관리 기능 구현
- 주문 생성 및 주문 상태 관리 로직 설계
- 결제 요청 및 처리 흐름 구현
- Spring Security + JWT 기반 인증/인가 구조 설계
- GitHub Actions 기반 CI/CD 환경 구성

## :busts_in_silhouette: Role Assignment
- 황민익 / 결제(Payment) & 리뷰(Review) 도메인, 아키텍처 및 AWS 인프라 설계 담당
- 김종표 / 주문(Order) & 리뷰(Review) 도메인 담당
- 박성준 / 사용자(User) 도메인 및 인증/인가 로직 담당
- 김다은 / 음식점(Store) & 음식(Menu) 도메인 담당

## :building_construction: Architecture
<img width="1197" height="723" alt="architecture" src="https://github.com/user-attachments/assets/ee643e3c-c7a6-403e-b617-dfa3774136fd" />


## :hammer_and_wrench: Tech Stack
- Java 17
- Spring Boot
- Spring Security + JWT
- JPA
- PostgreSQL
- Redis
- Docker
- GitHub Actions
- AWS EC2

## :card_file_box: ERD
![ERD](https://github.com/user-attachments/assets/239a893e-cd9c-45ca-aba7-8d8c8df32c09)


## :globe_with_meridians: Deployment
본 프로젝트는 AWS EC2 환경에 Docker 기반으로 배포되었습니다.

## :link: Production Server
http://43.200.96.77:8080/frontend/index.html

현재 EC2 인스턴스에서 실행 중이며, GitHub Actions를 통해 자동 배포됩니다.

## :books: API Documentation (Swagger)
http://43.200.96.77:8080/swagger-ui/index.html

Swagger를 통해 API 명세를 확인하고 직접 테스트할 수 있습니다.

## :hammer_and_wrench: Local Execution (Optional)
1. Repository Clone

```
git clone https://github.com/Suratgan/Suratgan.git
cd Suratgan
```

2. 환경 설정



```
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/suratgan
    username: your_db_username
    password: your_db_password
```

3. 실행



```
./gradlew bootRun
```



## :rocket: CI/CD
- GitHub Actions 기반 자동 빌드
- Docker Image 생성 후 Registry Push
- EC2에서 Docker Pull 후 재배포
