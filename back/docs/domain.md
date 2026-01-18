# 도메인 설계

## 엔티티

### Member (회원)

| 필드 | 타입 | 설명 |
|------|------|------|
| id | Long | PK, Auto Increment |
| email | String | 이메일 (로그인 ID) |
| password | String | 비밀번호 (암호화) |
| nickname | String | 닉네임 |
| createdAt | LocalDateTime | 가입일시 |
| updatedAt | LocalDateTime | 수정일시 |

### ShortenUrl (단축 URL)

| 필드 | 타입 | 설명 |
|------|------|------|
| id | Long | PK, Auto Increment (전체 공간 코드로 사용) |
| member | Member | FK, 생성한 회원 |
| personalCode | Long | 개인 공간 코드 (회원별 시퀀스) |
| originalUrl | String | 원본 URL |
| lastAccessedAt | LocalDateTime | 마지막 접근일시 |
| accessCount | Long | 접근 횟수 |
| createdAt | LocalDateTime | 생성일시 |
| updatedAt | LocalDateTime | 수정일시 |

## URL 구조

### 전체 공간
```
to2.at/{id}
```
- `id`: ShortenUrl의 PK (전역 시퀀스)
- 1년간 미사용 시 자동 삭제

### 개인 공간
```
to2.at/{memberId}/{personalCode}
```
- `memberId`: 회원 PK
- `personalCode`: 회원별 시퀀스
- 영구 보관

## 자동 삭제 정책

### 전체 공간 삭제 조건
- `lastAccessedAt`이 현재로부터 1년 이상 경과한 경우
- 스케줄러가 주기적으로 삭제 대상 조회 및 삭제 수행

### 개인 공간
- 삭제되지 않음 (영구 보관)
- 전체 공간이 삭제되어도 개인 공간 URL은 유지됨
