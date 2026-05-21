# SwiftChat 🚀

SwiftChat is a highly scalable, real-time chat application backend built with **Spring Boot**. Designed to handle high-throughput messaging, it features real-time communication via WebSockets, distributed message routing using Redis Pub/Sub, asynchronous full-text search indexing with RabbitMQ and Elasticsearch, and robust rate limiting.

## 🌟 Key Features

- **Real-Time Messaging:** Low-latency, bi-directional communication using WebSockets and the STOMP protocol.
- **Scalable Architecture:** Uses **Redis Pub/Sub** to broadcast WebSocket messages across multiple server instances.
- **Lightning-Fast Search:** Messages are asynchronously synced to **Elasticsearch** via **RabbitMQ**.
- **Spam Protection:** Built-in **Rate Limiting** via Redis.
- **Presence Tracking:** Real-time online/offline status and "last seen".
- **Chat Management:** Supports Private and Group chats with distributed locking.
- **Message Statuses:** Tracks `SENT`, `DELIVERED`, and `READ`.
- **Typing Indicators:** Real-time typing awareness.
- **Optimized Performance:** Redis caching + pagination for efficient loading.

---

## 🛠️ Tech Stack

- **Language:** Java 17+
- **Framework:** Spring Boot
- **Database:** MySQL / PostgreSQL
- **Cache & Pub/Sub:** Redis
- **Message Broker:** RabbitMQ
- **Search Engine:** Elasticsearch
- **Build Tool:** Maven

---

## 📂 Project Structure

```text
com.rishit.SwiftChat
├── config        # Redis, RabbitMQ, WebSocket configurations
├── controller    # REST APIs and WebSocket controllers
├── document      # Elasticsearch models
├── dto           # Request/Response DTOs
├── messaging     # RabbitMQ producers and Redis subscribers
├── model         # JPA Entities
├── repository    # JPA and Elasticsearch repositories
└── services      # Business logic
```

---

## 🚀 Getting Started

### Prerequisites

Ensure these services are installed and running:

- Java 17+
- MySQL or PostgreSQL
- Redis (Port 6379)
- RabbitMQ (Port 5672)
- Elasticsearch (Port 9200)

### Installation

#### Clone the repository

```bash
git clone https://github.com/yourusername/SwiftChat.git
cd SwiftChat
```

#### Configure your database and services

Update `src/main/resources/application.properties` with:

- Database credentials
- Redis configuration
- RabbitMQ configuration
- Elasticsearch configuration

#### Build the project

```bash
mvn clean install
```

#### Run the application

```bash
mvn spring-boot:run
```

---

## 🔌 API Reference (REST)

### User Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/user/save` | Create a new user |
| GET | `/user/get/{userId}` | Fetch user details (Redis Cached) |
| PUT | `/user/update/{userId}` | Update user details |
| DELETE | `/user/delete/{userId}` | Delete a user |

### Chat Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/chat/private` | Create a 1-on-1 chat |
| POST | `/chat/group` | Create a group chat |
| GET | `/chat/user/{userId}` | Get all chats for a user |
| POST | `/chat/{chatId}/add/{userId}` | Add user to existing chat |

### Message Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/message/sendMessage/{chatId}/{userId}` | Send fallback REST message |
| GET | `/message/getAllMessage/{chatId}?page=0&size=20` | Paginated chat history |
| PUT | `/message/readAll/{chatId}/user/{userId}` | Mark all as read |
| GET | `/message/search/{chatId}?keyword=...` | Search messages |

---

## 📡 WebSocket API (STOMP)

### Connection Endpoint

```text
/chat
```

### Subscriptions

```text
/topic/chat/{chatId}           → New messages
/topic/chat/{chatId}/typing    → Typing indicators
/topic/chat/{chatId}/status    → Read receipts
/topic/public                  → Presence events
```

### Publications

```text
/app/chat.sendMessage
/app/chat.typing
/app/chat.updateStatus
```

---

## 🧠 Core Architectural Concepts

### RabbitMQ for Search Indexing

When a message is sent:

1. Save message in primary database
2. Publish `NewMessageEvent` to RabbitMQ
3. Consumer processes event
4. Index into Elasticsearch

This keeps real-time messaging fast.

---

### Redis Pub/Sub for WebSocket Scaling

Without Redis:

- User A on Server 1
- User B on Server 2
- Messages won't sync

With Redis Pub/Sub:

Messages are broadcast across all instances so users can communicate regardless of connected server.

---

### Distributed Lock for Private Chat Creation

To prevent duplicate private chats:

```java
opsForValue().setIfAbsent(...)
```

A Redis lock with TTL ensures only one chat gets created.

---

## 📄 License

This project is licensed under the **MIT License**.
