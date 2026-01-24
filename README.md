# Event Booking App

## Objective
This document provides a detailed, step-by-step explanation of:
- Introduction
- Steps to instructions on how to set up and run your project 

---

## Requirements
- **Java**: Version 17 or later
- **Maven**: Version latest
- **Docker**: Version 20.10 or later  
- **Docker Compose**: Version 1.29 or later  
- **GitHub Repository**:  
  https://github.com/Harsh-0806810033/EventBooking.git

---

## 🗂️ Repository Structure
├── Dockerfile
├── docker-compose.yml
├── .env.example
├── README.md
└── src/


---

## 🐳 Docker Image Information

### Docker Image
- **Image Name:** `harshnagarro/event-booking-app`
- **Version:** `1.0`
- **Registry:** Docker Hub (Private Repository)


---

## 🚀 Instructions to Set Up and Run the Project

This project can be run in **two ways**:
1. **Using Dockerfile (build Docker image locally)**
2. **Using prebuilt Docker image**

---

## 🐳 Option 1: Run Using Dockerfile (Build Image Locally)

### Steps

**Clone the repository**

```bash
git clone https://github.com/Harsh-0806810033/EventBooking.git
````

**Navigate into the project folder**

```bash
cd EventBooking
```

**Build the JAR file**

```bash
mvn clean package
```

**Update docker-compose.yml**

* Uncomment `service.app.build`
* Comment `service.app.image`

Example:

```yaml
app:
  build: .
  # image: harshnagarro/event-booking-app:1.0
```

**Build Docker image and start containers**

```bash
docker compose up --build -d
```

**Verify running containers**

```bash
docker compose ps
```

**Check application logs**

```bash
docker logs -f eventmanagement_app
```

**Access the application**

```
http://localhost:8080
```

**Login credentials**

```
Username: admin@test.com
Password: password
```

---

## 🐳 Option 2: Run Using Prebuilt Docker Image

### Steps

**Clone the repository**

```bash
git clone https://github.com/Harsh-0806810033/EventBooking.git
```

**Navigate into the project folder**

```bash
cd EventBooking
```

**Update `docker-compose.yml`**

* Comment `service.app.build`
* Uncomment `service.app.image`

Example:

```yaml
app:
  # build: .
  image: harshnagarro/event-booking-app:1.0
```

**Start the containers**

```bash
docker compose up -d
```

**Verify running containers**

```bash
docker compose ps
```

**Check application logs**

```bash
docker logs -f eventmanagement_app
```

**Access the application**

```
http://localhost:8080
```

**Login credentials**

```
Username: admin@test.com
Password: password
```

---

## 🧹 Stop the Application

Stop containers:

```bash
docker compose down
```

Stop containers and remove volumes (database data):

```bash
docker compose down -v
```

