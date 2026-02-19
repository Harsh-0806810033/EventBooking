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
- - **GitHub Branch**: docker-advance

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
- **Version:** `2.0`
- **Registry:** Docker Hub (Private Repository)


---

## 🚀 Instructions to Set Up and Run the Project

This project can be run with below steps:
1. **Docker image already build and pushed in docker hub repository: harshnagarro/event-booking-app**
2. **Using same image in docker-compose.yml, we use docker-compose to deploy application.**
---

## 🐳 Run Using Prebuilt Docker Image (docker compose)

### Steps

**Clone the repository**

```bash
git clone https://github.com/Harsh-0806810033/EventBooking.git
```

**Git checkout to branch - docker-advance**

```bash
git checkout -b docker-advance origin/docker-advance
```

**Navigate into the project folder**

```bash
cd EventBooking
```

**View `docker-compose.yml`**

* Make sure it has below image

Example:

```yaml
app:
  # build: .
  image: harshnagarro/event-booking-app:2.0
```

**Start the containers**

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

## 🧹 Stop the Application

Stop containers:

```bash
docker compose down
```

Stop containers and remove volumes (database data):

```bash
docker compose down -v
```

