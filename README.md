# ✈️ 𝗧𝗼𝘂𝗿𝗠𝗮𝘁𝗲-𝗕𝗮𝗰𝗸𝗲𝗻𝗱 ✈️

> [TourMate 프로젝트 설명 바로 가기](https://github.com/korea-tour-mate-app)

</br>

## 📂 𝗣𝗿𝗼𝗷𝗲𝗰𝘁 𝗙𝗼𝗹𝗱𝗲𝗿 𝗦𝘁𝗿𝘂𝗰𝘁𝘂𝗿𝗲
```plaintext
tourmate-backend
├── src
│   └── main
│       └── java
│           └── com
│               └── snowflake_guide
│                   └── tourmate
│                       ├── domain                      # 비즈니스 도메인 관련 코드
│                       │   ├── baggage_storage         # 짐 보관 장소
│                       │   ├── like                    # 좋아요 장소
│                       │   ├── member                  # 회원 관리
│                       │   ├── place                   # 테마별 장소
│                       │   ├── restaurant              # 서울 인기 식당
│                       │   │   ├── api                 # 레스토랑 관련 API 컨트롤러
│                       │   │   ├── dto                 # 레스토랑 관련 데이터 전송 객체
│                       │   │   ├── entity              # 레스토랑 도메인 엔티티 클래스
│                       │   │   ├── repository          # 레스토랑 관련 데이터 접근 레이어
│                       │   │   └── service             # 레스토랑 관련 비즈니스 로직
│                       │   ├── restaurant_review       # 서울 인기 식당 리뷰
│                       │   ├── review                  # 회원 자체 리뷰
│                       │   ├── theme                   # 테마
│                       │   ├── visited_place           # 방문한 장소
│                       └── global                      # 공통 모듈
│                           ├── auth                    # 인증/인가 관련 기능
│                           │   ├── email               # 이메일 인증 관련 모듈
│                           ├── client                  # 외부 API 통신 모듈
│                           ├── config                  # 설정 관련 파일
│                           ├── exception               # 예외 처리
│                           └── util                    # 유틸리티 클래스
│                       └── TourmateApplication         # 메인 애플리케이션
│       └── resources
│             ├── application.yml                       # 애플리케이션 설정 파일
├── .gitignore                                           # Git에 포함되지 않을 파일 목록
├── build.gradle                                         # Gradle 빌드 파일
└── Dockerfile                                           # Docker 설정 파일
```

</br>

## 🔧 𝗧𝗲𝗰𝗵 𝗦𝘁𝗮𝗰𝗸
- Spring Boot
- Spring Data JPA
- Spring Security
- MySQL, AWS S3
- AWS EC2, Github Actions, Docker

</br>


## ✨ 𝗕𝗿𝗮𝗻𝗰𝗵 𝗖𝗼𝗻𝘃𝗲𝗻𝘁𝗶𝗼𝗻𝘀

| **브랜치 유형**      | **설명**                                                                 |
|--------------------|-------------------------------------------------------------------------|
| `develop`             | 개발 코드                                                |
| `feature/#<이슈번호>-<기능명>`  | 새로운 기능 개발 브랜치                                                  |
| `main`   | 배포한 최종 코드                                                       |

</br>
