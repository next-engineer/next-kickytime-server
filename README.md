# next-kickytime-server

> Kickytime 백엔드 – Spring Boot 기반 풋살 매칭 API 서버. AWS Cognito 인증, PostgreSQL 연동, ECS/EC2 혼합 배포, CI/CD 지원.

- **프로젝트명**: Kickytime
- **프로젝트 기간**: 2025-08-01 ~ 2025-08-14

---

## 팀원

| 이름 | GitHub                                               |
| --- |------------------------------------------------------|
| 박민지 | [https://github.com/Mminzy22](https://github.com/Mminzy22)             |
| 하영현 | [https://github.com/deepInTheWoodz](https://github.com/deepInTheWoodz) |
| 이혜민 | [https://github.com/hyeminleeee](https://github.com/hyeminleeee)       |
| 구태연 | [https://github.com/taeyeon119](https://github.com/taeyeon119)         |
| 임정우 | [https://github.com/imjwoo](https://github.com/imjwoo)                 |

---

[![Docs: Wiki](https://img.shields.io/badge/docs-Wiki-0366d6)](../../wiki)

> 자세한 운영 가이드는 GitHub **Wiki**에서 확인하세요.

### 📚 Wiki 빠른 링크

* [Home](../../wiki)
* [API 문서](../../wiki/API-문서)
* [브랜치 전략](../../wiki/브랜치-전략)
* [버전 관리 전략](../../wiki/버전-관리-전략)
* [이슈 템플릿 가이드](../../wiki/이슈-템플릿-가이드)
* [커밋 메시지 규칙](../../wiki/커밋-메시지-규칙)
* [코드 스타일 자동화 설정](../../wiki/코드-스타일-자동화-설정)
* [풀리퀘스트 템플릿](../../wiki/풀리퀘스트-템플릿)

---

## TL;DR

```bash
# 요구사항: JDK 17, PostgreSQL(또는 Docker), Gradle Wrapper
./gradlew clean build
./gradlew bootRun
```

---

## 1) 개요

많은 사용자가 풋살 경기에 참여하고 싶어하지만, 기존 오프라인/커뮤니티 중심 매칭은 번거롭고 참여자 현황 관리가 어렵습니다. Kickytime은 **관리자가 개설한 경기 정보**를 바탕으로 **사용자들이 참여/취소**할 수 있도록 돕는 서비스입니다.

**핵심 가치**

* 간편한 매칭 생성·참여
* 인원 제한 및 중복 참여 방지
* AWS Cognito 기반 인증 (향후 Spring Security + JWT로 확장)

---

## 2) 아키텍처(Architecture)

* **라우팅**: ALB가 `/api/match*`는 **EC2**(오토 스케일링 그룹)로, 그 외 `/api/*`는 **ECS Fargate**로 라우팅
* **네트워킹**: VPC 멀티 AZ, 퍼블릭/프라이빗 서브넷, NAT 게이트웨이
* **데이터**: RDS(PostgreSQL)
* **운영**: CloudWatch 로그, SSM(Systems Manager), KMS
<img width="1401" height="756" alt="4팀 Project drawio" src="https://github.com/user-attachments/assets/b8e7fb3e-c7b7-4f85-afa2-b0e7e751b1aa" />


---

## 3) 주요 기능

### 사용자

* 매칭 목록 조회(제목/시간/장소)
* 매칭 **참여/취소** (인원 제한, 중복 참여 방지)
* 회원가입 이메일 검증(Cognito) / 기본 프로필 이미지

### 관리자

* 매칭 개설(시간/장소/인원)
* 매칭 삭제(참여자 포함 정리)

---

## 4) API 개요

> **인증 헤더**: `Authorization: Bearer <ACCESS_TOKEN>` (Cognito Access Token)

### 사용자/인증

* `POST /api/users/signin-up` – 최초 로그인 시 `cognito_sub` 기준 upsert
* `GET /api/users/me` – 내 프로필 조회

### 매칭

* `GET /api/matches` – 매칭 목록 조회
* `GET /api/matches/me` – 내가 참여한 매칭 조회
* `POST /api/matches/{matchId}/participants` – 매칭 참여
* `DELETE /api/matches/{matchId}/participants` – 매칭 취소
* `DELETE /api/matches/{matchId}` – (관리자) 매칭 삭제


자세히 보기: [API 문서](../../wiki/API-문서)

---

## 5) 인증 흐름(Auth Flow)

1. 사용자가 Cognito Hosted UI에서 회원가입/로그인
2. Cognito가 이메일 검증 및 토큰(JWT) 발급
3. 백엔드는 최초 로그인 시 `cognito_sub`로 **users upsert**
4. 이후 요청은 `Bearer` 토큰으로 인증 → 백엔드에서 JWK 검증

---

## 6) 로컬 시작하기(Getting Started)

### 사전 준비(Prerequisites)

* JDK(제이디케이) 17+, Gradle(그레이들) Wrapper
* PostgreSQL 17+ (로컬 or Docker(도커))

### 환경 변수/설정 예시

`application-dev.yml`

```yaml
# 개발 환경 설정
spring:
  datasource:
    url: ${DB_URL:jdbc:postgresql://localhost:5432/kickytime}
    username: ${DB_USERNAME:postgres}
    password: ${DB_PASSWORD:postgres}

  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true

app:
  cors:
    allowed-origins: "http://localhost:5173"
```

### 실행

```bash
./gradlew clean build
./gradlew bootRun
```

### 테스트

```bash
./gradlew test
```

---

## 7) 배포 개요(Deployment)

* **아키텍처**: `/api/match*` → **EC2 ASG**, 그 외 `/api/*` → **ECS Fargate**
* **파이프라인**: GitHub Actions – 빌드/테스트 → 이미지 푸시 → 서비스 업데이트, (FE 별도) S3/CloudFront 정적 배포
* **운영/보안**: CloudWatch 로깅, VPC 분리, SG 최소권한, SSM, KMS

자세히 보기: [버전 관리 전략](../../wiki/버전-관리-전략)

---

## 8) 개발 규칙 & 협업 가이드

* **브랜치 전략**: `main`, `develop`, `feature/*`, `/hotfix/*` (보호 브랜치, PR 필수, 승인 ≥ 1)
* **커밋 컨벤션**: Conventional Commits + Gitmoji 사용
* **코드 스타일/훅**: BE – Spotless/Checkstyle, (FE 별도) Prettier/ESLint, Husky
* **PR/이슈 템플릿**: Wiki 제공 템플릿 사용

자세히 보기:
- [브랜치 전략](../../wiki/브랜치-전략)
- [커밋 메시지 규칙](../../wiki/커밋-메시지-규칙)
- [이슈 템플릿 가이드](../../wiki/이슈-템플릿-가이드)
- [풀리퀘스트 템플릿](../../wiki/풀리퀘스트-템플릿)
- [코드 스타일 자동화 설정](../../wiki/코드-스타일-자동화-설정)
