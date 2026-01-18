# API 문서

## Base URL
```
https://api.to2.at
```

## 인증
모든 API는 로그인이 필요합니다 (리다이렉트 API 제외).

### 인증 방식
- JWT Bearer Token
- Header: `Authorization: Bearer {token}`

## API 목록

| API | 설명 |
|-----|------|
| [회원 API](./member.md) | 회원가입, 로그인, 내 정보 조회 |
| [URL 단축 API](./shortenUrl.md) | URL 단축, 목록 조회, 리다이렉트 |

## 공통 응답 형식

### 성공
```json
{
  "code": "200-1",
  "msg": "성공",
  "data": { ... }
}
```

### 실패
```json
{
  "code": "400-1",
  "msg": "에러 메시지",
  "data": null
}
```

## 공통 에러 코드

| 코드 | 설명 |
|------|------|
| 400-1 | 잘못된 요청 |
| 401-1 | 인증 필요 |
| 403-1 | 권한 없음 |
| 404-1 | 리소스 없음 |
| 500-1 | 서버 에러 |
