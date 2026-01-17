# Reactive Content Platform

## Requirements
- Java 21
- Maven
- Docker

## Run MongoDB
```bash
docker run -d --name exam-mongo -p 27018:27017 -e MONGO_INITDB_DATABASE=blogdb mongo:latest
