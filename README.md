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

---

## 🚀 Below are the Improvements we have done with this
Improvements:
1. **Used multi-stage builds where appropriate.**
2. **Configured Docker to run containers with the least privilege.**
3. **Set up a custom Docker network and connect multiple containers to it.**
4. **Used Docker volumes to persist data for your containers.**
5. **Used named volumes and bind mounts**
6. **Used tools like Docker Bench for Security to audit Docker environment.**
---

## 🐳 Lets go through each points

### Points

**Used multi-stage builds where appropriate.**

```dockerfile
# -------- Stage 1: Build --------
FROM maven:3.9.6-eclipse-temurin-17 AS build

WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src
RUN mvn clean package -DskipTests

# -------- Stage 2: Runtime --------
FROM eclipse-temurin:17-jre-alpine

RUN addgroup -S spring && adduser -S spring -G spring
WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

RUN chown spring:spring app.jar
USER spring

EXPOSE 8080

ENTRYPOINT ["java", "-XX:+UseContainerSupport", "-jar", "app.jar"]

```

**Configured Docker to run containers with the least privilege.**

```dockerfile
RUN addgroup -S spring && adduser -S spring -G spring
```
Creates a non-root user named spring.
* addgroup -S spring → create system group
* adduser -S spring -G spring → create system user in that group
* Running containers as root is unsafe. This improves security.

**Set up a custom Docker network and connect multiple containers to it.**

```yaml
# ✅ Custom Network for Multi-Container Communication
networks:
  event_network:
    driver: bridge
```

**Uses Docker volumes to persist data for your containers.**

```yaml
# ✅ Named Volume Definition
volumes:
  mysql_data:
    driver: local
```
* Used above volume with mysql_db persistance so that if it would restart data would not lost.
* details mentioned in docker-compose.yml also

**Used named volumes and bind mounts**

* Binding container `logs` folder with host machine folder `app-logs`. Even if container is deleted, logs remain on host.

```yaml
    volumes:
      # ✅ Bind Mount (Logs stored on host machine)
      - ./app-logs:/logs
```

**Used tools like Docker Bench for Security to audit Docker environment.**

```bash
docker run --rm -it \
  --net host \
  --pid host \
  --userns host \
  --cap-add audit_control \
  -v /var/lib:/var/lib \
  -v /var/run/docker.sock:/var/run/docker.sock \
  -v /etc:/etc \
  -v /usr/bin/containerd:/usr/bin/containerd \
  docker/docker-bench-security

```

* It checks your system against Docker’s official security guidelines (CIS Benchmarks).