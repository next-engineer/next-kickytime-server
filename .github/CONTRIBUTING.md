# 🤝 Contributing Guidelines

이 문서는 Kickytime 프로젝트에 기여하는 방법과 규칙을 설명합니다.  
모든 팀원은 아래 규칙을 준수해야 하며, 새로운 팀원이 합류할 때 반드시 숙지해야 합니다.

---

## 1. 브랜치 전략

### 선택: GitLab Flow – 단순 브랜치형

| 브랜치 | 설명 |
| --- | --- |
| `main` | 운영 환경(Production) 배포용 |
| `develop` | 개발 통합 브랜치, 기능 병합 전 테스트 공간 |
| `feature/*` | 기능별 브랜치 (`feature/이슈번호-간단설명`) |
| `hotfix/*` | 운영 중 발생한 긴급 수정 브랜치 |

**운영 규칙**
- `main`, `develop`은 보호 브랜치로 설정하고, 직접 푸시 금지
- 모든 변경은 Pull Request(PR)를 통해 병합
- 최소 1명 이상의 코드 리뷰 승인 필수
- 브랜치명 예시:
  - `feature/123-user-login`
  - `hotfix/456-admin-crash`

---

## 2. 커밋 메시지 전략

**규칙**: [Conventional Commits](https://www.conventionalcommits.org/ko/v1.0.0/) + [Gitmoji](https://gitmoji.dev/)  
```

<이모지> <타입>(선택적 scope): <커밋 메시지>

```

### 타입 & 이모지 예시

| 이모지 | 타입 | 예시 | 설명 |
| --- | --- | --- | --- |
| ✨ | `feat` | ✨ feat(auth): 사용자 대시보드 페이지 추가 | 새로운 기능 개발 |
| 🐛 | `fix` | 🐛 fix(login): 로그인 실패 시 예외 처리 추가 | 사용자 영향 버그 수정 |
| 🚨 | `fix` | 🚨 fix(test): 테스트 실패 원인 수정 | 테스트/빌드 오류 수정 |
| ♻️ | `refactor` | ♻️ refactor: 중복 조건문 제거 | 기능 변화 없는 구조 개선 |
| 📄 | `docs` | 📄 docs: 배포 방법 문서 추가 | 문서 수정/주석 |
| 🎨 | `style` | 🎨 style: 공백 및 줄 정리 | 스타일 변경 (로직 X) |
| 🧱 | `arch` | 🧱 arch: DDD 계층 구조로 변경 | 전체 아키텍처 변경 |
| 🏗️ | `setup` | 🏗️ setup: Spring Boot 초기 설정 | 프로젝트 셋업 |
| 🔧 | `config` | 🔧 config: DB 설정 추가 | 설정 파일 변경 |
| 📦 | `chore` | 📦 chore: 라이브러리 버전 정리 | 유지보수/빌드 관리 |
| 🧪 | `test` | 🧪 test(user): 사용자 조회 API 테스트 추가 | 테스트 코드 |
| 💄 | `ui` | 💄 ui: 버튼 색상 변경 | UI 수정 |
| 📐 | `design` | 📐 design: 메인 페이지 레이아웃 구성 | 디자인/레이아웃 |
| ⬆️ | `upgrade` | ⬆️ upgrade: React 18로 업그레이드 | 의존성 업 |
| ⬇️ | `downgrade` | ⬇️ downgrade: Java 17→11 | 의존성 다운 |
| 🔥 | `remove` | 🔥 remove: 사용되지 않는 모듈 삭제 | 대규모 삭제 |
| ✏️ | `fix` | ✏️ fix: 변수명 오타 수정 | 오타 수정 |

---

## 3. 버전(태그) 전략

- [Semantic Versioning](https://semver.org/lang/ko/) 적용
- 태그 형식: `vMAJOR.MINOR.PATCH`
  - 예: `v1.0.0`, `v1.2.3`
- MAJOR: 호환 불가능 변경
- MINOR: 기능 추가(호환성 유지)
- PATCH: 버그 수정(호환성 유지)

---

## 4. Pull Request(PR) 규칙

- PR은 반드시 **PR 템플릿**을 사용
- 브랜치 → `develop` 병합 (운영 반영 시 `main` 병합)
- 최소 1명 이상 코드 리뷰 승인 필수
- 관련 이슈 번호 반드시 연결 (예: `Ref: #123`)

---

## 5. Issue 템플릿

### 제공되는 템플릿 종류
- ✨ 기능 요청 (Feature request)
- 🐛 버그 보고 (Bug report)
- 📝 문서 변경 요청 (Documentation)
- 🛠 작업(Task)

이슈 생성 시 해당 유형을 선택하고 양식에 맞춰 작성합니다.

---

## 6. 코드 스타일 & 린트

- **FE**: Prettier + ESLint 규칙 적용, 자동 포맷 커밋 훅
- **BE**: Checkstyle / Spotless 설정
- **CI**: 포맷 실패 시 빌드 실패 처리

---

## 7. Git Hooks 운영

- **pre-commit**: 린트, 테스트, 포맷 검사
- **commit-msg**: 커밋 메시지 포맷 검사
- 사용 도구: [Husky](https://typicode.github.io/husky/)

---

## 8. 참고 링크
- [Conventional Commits](https://www.conventionalcommits.org/ko/v1.0.0/)
- [Gitmoji](https://gitmoji.dev/)
- [Semantic Versioning](https://semver.org/lang/ko/)
