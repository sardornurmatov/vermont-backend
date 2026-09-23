# Vermont Java Backend

Vermont marketplace uchun mustaqil **Java 21 + Spring Boot + PostgreSQL** REST API.

## Texnologiyalar
- Spring Boot 3.3
- Spring Security + JWT
- Spring Data JPA / Hibernate
- PostgreSQL + Flyway
- BCrypt
- Maven

## Lokal ishga tushirish
1. PostgreSQL bazasini yarating: `vermont_db`.
2. Muhit o‘zgaruvchilarini belgilang:

```powershell
$env:DATABASE_URL="jdbc:postgresql://localhost:5432/vermont_db"
$env:DB_USER="postgres"
$env:DB_PASSWORD="postgres-parolingiz"
$env:JWT_SECRET="kamida-32-belgili-juda-uzun-maxfiy-kalit"
$env:ADMIN_PASSWORD="kuchli-admin-paroli"
$env:FRONTEND_ORIGIN="http://localhost:5500"
```

3. Serverni ishga tushiring:

```powershell
mvn spring-boot:run
```

Server: `http://localhost:8080`

## Docker bilan
```bash
docker compose up --build
```

## Frontendni ulash
Vermont frontenddagi `js/config.js` faylida:
```js
API_BASE_URL: 'http://localhost:8080/api'
```

## API
- `POST /api/customers/register`
- `POST /api/customers/login`
- `POST /api/auth/admin/login`
- `GET /api/products`
- `POST/PATCH/DELETE /api/products`
- `GET/POST /api/orders`
- `GET /api/orders/mine`
- `PATCH /api/orders/{id}/status`
- `GET/PUT /api/payment-info`
- `GET /api/stats`

Flyway server ishga tushganda jadvallar va boshlang‘ich mahsulotlarni avtomatik yaratadi.
