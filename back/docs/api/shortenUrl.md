# URL 단축 API

## URL 단축

### Request
```http
POST /api/shorten-urls
Authorization: Bearer {accessToken}
Content-Type: application/json

{
  "originalUrl": "https://example.com/very/long/url/path?query=param"
}
```

### Response
```json
{
  "code": "201-1",
  "msg": "URL이 단축되었습니다.",
  "data": {
    "id": 42,
    "originalUrl": "https://example.com/very/long/url/path?query=param",
    "globalUrl": "https://to2.at/42",
    "personalUrl": "https://to2.at/5/3",
    "createdAt": "2024-01-15T10:30:00"
  }
}
```

- `globalUrl`: 전체 공간 URL (1년 미사용 시 삭제)
- `personalUrl`: 개인 공간 URL (영구 보관)

---

## 내 단축 URL 목록 조회

### Request
```http
GET /api/shorten-urls/me?page=0&size=10
Authorization: Bearer {accessToken}
```

### Response
```json
{
  "code": "200-1",
  "msg": "성공",
  "data": {
    "content": [
      {
        "id": 42,
        "personalCode": 3,
        "originalUrl": "https://example.com/very/long/url/path",
        "globalUrl": "https://to2.at/42",
        "personalUrl": "https://to2.at/5/3",
        "accessCount": 150,
        "lastAccessedAt": "2024-01-15T10:30:00",
        "createdAt": "2024-01-01T09:00:00"
      }
    ],
    "pageable": {
      "page": 0,
      "size": 10,
      "totalElements": 25,
      "totalPages": 3
    }
  }
}
```

---

## 리다이렉트 (전체 공간)

### Request
```http
GET /{globalCode}
```

예시: `GET /42`

### Response
- **성공**: `302 Redirect` → 원본 URL로 이동
- **실패**: `404 Not Found` (삭제되었거나 존재하지 않는 경우)

---

## 리다이렉트 (개인 공간)

### Request
```http
GET /{memberId}/{personalCode}
```

예시: `GET /5/3`

### Response
- **성공**: `302 Redirect` → 원본 URL로 이동
- **실패**: `404 Not Found` (존재하지 않는 경우)

---

## 단축 URL 삭제

### Request
```http
DELETE /api/shorten-urls/{id}
Authorization: Bearer {accessToken}
```

### Response
```json
{
  "code": "200-1",
  "msg": "삭제되었습니다.",
  "data": null
}
```

- 본인이 생성한 URL만 삭제 가능
- 삭제 시 전체 공간, 개인 공간 모두 삭제됨
