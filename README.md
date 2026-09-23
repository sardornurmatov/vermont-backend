# Vermont Java Backend

Vermont marketplace uchun Java 21 va Spring Boot asosidagi PostgreSQL REST API.

## Texnologiyalar

- Java 21
- Spring Boot 3.3
- Spring Security + JWT
- Spring Data JPA / Hibernate
- PostgreSQL + Flyway
- BCrypt
- Maven

## Talablar

- JDK 21 yoki undan yuqori
- Maven 3.9 yoki undan yuqori
- Lokal PostgreSQL serveri

Docker konfiguratsiyasi loyihadan olib tashlangan. Ilova faqat lokal yoki alohida ishlayotgan PostgreSQL serveriga ulanadi.

## PostgreSQL bazasini tayyorlash

PostgreSQL administratori sifatida bazani yarating:

```sql
CREATE DATABASE vermont_db;
```

Agar `postgres` foydalanuvchisi parolini bilmasangiz yoki xatolik chiqsa, parolni yangilang:

```sql
ALTER ROLE postgres WITH LOGIN PASSWORD '<YOUR_POSTGRES_PASSWORD>';
```

Ulanishni ilovani ishga tushirishdan oldin tekshiring:

```bash
psql -h localhost -p 5432 -U postgres -d vermont_db -W
```

`SQL State: 28P01` chiqsa, ilovadagi `DB_PASSWORD` PostgreSQL’dagi haqiqiy parol bilan mos emas.

## Muhit o‘zgaruvchilari

`.env.example` faylidan nusxa olib, qiymatlarni to‘ldiring. `.env` fayli avtomatik yuklanmaydi; terminal sessiyasida o‘zgaruvchilarni eksport qiling.

### Windows PowerShell

```powershell
$env:DATABASE_URL="jdbc:postgresql://localhost:5432/vermont_db"
$env:DB_USER="postgres"
$env:DB_PASSWORD="<YOUR_POSTGRES_PASSWORD>"
$env:JWT_SECRET="<AT_LEAST_32_CHARACTER_RANDOM_SECRET>"
$env:ADMIN_PASSWORD="<STRONG_ADMIN_PASSWORD>"
$env:FRONTEND_ORIGIN="http://localhost:5500"
```

### Linux yoki macOS

```bash
export DATABASE_URL="jdbc:postgresql://localhost:5432/vermont_db"
export DB_USER="postgres"
export DB_PASSWORD="<YOUR_POSTGRES_PASSWORD>"
export JWT_SECRET="<AT_LEAST_32_CHARACTER_RANDOM_SECRET>"
export ADMIN_PASSWORD="<STRONG_ADMIN_PASSWORD>"
export FRONTEND_ORIGIN="http://localhost:5500"
```

Parollarni GitHub’ga, `README.md` fayliga yoki loglarga joylamang.

## Ishga tushirish

```bash
mvn --batch-mode spring-boot:run
```

Server manzili: `http://localhost:8080`

Flyway server ishga tushganda jadvallar va boshlang‘ich mahsulotlarni avtomatik yaratadi. `ddl-auto: validate` ishlatilgani uchun migration muvaffaqiyatli bajarilmasa, ilova ishga tushmaydi.

## Frontendni ulash

Vermont frontenddagi `js/config.js` faylida:

```js
API_BASE_URL: 'http://localhost:8080/api'
```

## API

- `GET /api/health`
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

## Muammoni tekshirish

Agar ilova `password authentication failed for user "postgres"` yoki `SQL State: 28P01` desa:

1. `DB_PASSWORD` qiymatini tekshiring.
2. `psql` orqali aynan shu credential bilan ulanib ko‘ring.
3. `DATABASE_URL` hosti, porti va baza nomini tekshiring.
4. Spring Boot’ning faol terminal sessiyasida environment o‘zgaruvchilari mavjudligini tekshiring.
5. PostgreSQL serveri ishlayotganini tasdiqlang.
