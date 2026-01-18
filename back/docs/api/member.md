# 회원 API

## 회원가입

### Request
```http
POST /api/members/join
Content-Type: application/json

{
  "email": "user@example.com",
  "password": "password123",
  "nickname": "홍길동"
}
```

### Response
```json
{
  "code": "201-1",
  "msg": "회원가입이 완료되었습니다.",
  "data": {
    "id": 1,
    "email": "user@example.com",
    "nickname": "홍길동"
  }
}
```

---

## 로그인

### Request
```http
POST /api/members/login
Content-Type: application/json

{
  "email": "user@example.com",
  "password": "password123"
}
```

### Response
```json
{
  "code": "200-1",
  "msg": "로그인 성공",
  "data": {
    "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
  }
}
```

---

## 내 정보 조회

### Request
```http
GET /api/members/me
Authorization: Bearer {accessToken}
```

### Response
```json
{
  "code": "200-1",
  "msg": "성공",
  "data": {
    "id": 1,
    "email": "user@example.com",
    "nickname": "홍길동",
    "createdAt": "2024-01-15T10:30:00"
  }
}
```

---

## 토큰 갱신

### Request
```http
POST /api/members/refresh
Content-Type: application/json

{
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

### Response
```json
{
  "code": "200-1",
  "msg": "토큰 갱신 성공",
  "data": {
    "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
  }
}
```
