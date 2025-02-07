# 🔗 URL Shortener Pro - Secure, Scalable & Analytics-Powered

Welcome to **URL Shortener Pro**! A high-performance, secure, and feature-rich URL shortening service built with Java Spring Boot. Perfect for businesses and developers needing robust link management, detailed analytics, and enterprise-grade security.

![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2%2B-brightgreen)
![Redis](https://img.shields.io/badge/Redis-Caching%20%26%20Rate%20Limiting-red)
![JWT](https://img.shields.io/badge/Security-JWT%20Role--Based-blueviolet)

## 🚀 Key Features

### 🔒 **JWT Role-Based Security**
- Secure authentication/authorization with **JSON Web Tokens (JWT)**
- Role-based access control: `ADMIN` (manage all URLs/users) and `USER` (create/manage own URLs)
- Token expiration and refresh mechanisms

### 📊 **Smart Analytics**
- Tracks every click with metadata:
  - Browser, OS, Device Type
  - IP Address, Geolocation
  - Timestamp, Referrer URL
- **Base62 Encoding** for generating short, collision-resistant URLs
- Admin dashboard to view analytics (future scope)

### ⚡ **Performance & Scalability**
- **Redis Caching**: Accelerates short URL redirection with sub-millisecond latency
- **Rate Limiting** via Bucket4j:
  - Protect against DDoS/abuse (e.g., 100 requests/minute per user)
  - Configurable buckets for dynamic throttling

### 🔮 **Future-Ready**
- [Upcoming] Custom short URLs (e.g., `yourbrand.co/mylink`)
- [Planned] Advanced analytics dashboard with graphs/export
- [Ideas] QR code support, bulk URL upload, API rate limit tiers

## 🛠️ Tech Stack

- **Backend**: Spring Boot 3.2+, Spring Security, Spring Data JPA
- **Authentication**: JWT (Java JWT Library)
- **Database**: H2 (Embedded), MySQL (Production-ready)
- **Caching & Rate Limiting**: Redis, Bucket4j
- **Encoding**: Custom Base62 Algorithm
- **Utilities**: Lombok, MapStruct, Actuator

## 🛠️ Installation

**Prerequisites**:
- Java 17+
- Redis server (local or remote)
- MySQL (optional) / H2 (default)

```bash
# 1. Clone the repo
git clone https://github.com/yourusername/url-shortener-pro.git

# 2. Configure application.properties
spring.datasource.url=jdbc:h2:mem:urlsdb
spring.jpa.hibernate.ddl-auto=update

jwt.secret=your-512-bit-secret
redis.host=localhost
redis.port=6379


# 3. Build & Run
./mvnw clean install
java -jar target/url-shortener-pro-1.0.0.jar

```

## 🌟 Future Roadmap
-Custom short URL slugs

-Multi-tier rate limiting (free/premium users)

-User dashboard with real-time analytics

-Prometheus/Grafana monitoring integration
