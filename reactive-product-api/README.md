# Reactive Product API (WebFlux + MongoDB)

## Requirements
- Java 21
- Maven
- Docker

## Run MongoDB (Docker)
```bash
docker run -d --name my-mongo-db -p 27017:27017 -e MONGO_INITDB_DATABASE=productdb mongo:latest

## Run the application
```bash
mvn spring-boot:run

## API Endpoints
- GET /api/products
- GET /api/products/{id}
- POST /api/products
- PUT /api/products/{id}
- DELETE /api/products/{id}


