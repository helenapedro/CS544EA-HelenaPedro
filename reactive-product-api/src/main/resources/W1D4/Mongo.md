* ### Lab Exercise: Reactive Product API with Spring WebFlux and MongoDB

  #### Objective

  This exercise aims to provide hands-on experience in building a reactive RESTful API using Spring WebFlux and Spring Data MongoDB. You will implement a basic CRUD (Create, Read, Update, Delete) API for a `Product` entity, interacting with a MongoDB database running as a Docker container.

  #### Technologies & Tools

    *   Java 21+
    *   Spring Boot 3.x
    *   Spring WebFlux
    *   Spring Data Reactive MongoDB
    *   Maven
    *   Docker
    *   An IDE (e.g., IntelliJ IDEA, VS Code, Eclipse)
    *   Postman, Insomnia, or cURL for API testing

  #### Task Description

  Your task is to create a Spring Boot application that exposes a reactive REST API for managing `Product` information, backed by MongoDB.

  #### 1. Set up MongoDB using Docker

  Before starting your Spring Boot application, you need to have a MongoDB instance running as a Docker container. Open your terminal or command prompt and run the following command to start a MongoDB container:

  ```bash
  docker run -d --name my-mongo-db -p 27017:27017 -e MONGO_INITDB_DATABASE=productdb mongo:latest
  ```

  This command will:
    *   `docker run -d`: Start a new container in detached mode (runs in the background).
    *   `--name my-mongo-db`: Assign the name `my-mongo-db` to this container.
    *   `-p 27017:27017`: Map port `27017` from your host machine to port `27017` inside the container (MongoDB's default port).
    *   `-e MONGO_INITDB_DATABASE=productdb`: Set an environment variable to initialize a database named `productdb`.
    *   `mongo:latest`: Use the latest MongoDB official image from Docker Hub.

  Ensure that your Spring Boot application can connect to this MongoDB instance.

  To stop the MongoDB container later (when you're done with the lab):
  ```bash
  docker stop my-mongo-db
  ```

  To remove the MongoDB container (if you want to free up resources or restart from scratch):
  ```bash
  docker rm my-mongo-db
  ```

  #### 2. Create a Spring Boot Project

  a.  Go to [Spring Initializr](https://start.spring.io/).

  b.  Configure your project with:
  *   **Project:** Maven Project
  *   **Language:** Java
  *   **Spring Boot:** 3.x (latest stable)
  *   **Group:** `edu.miu.cs544`
  *   **Artifact:** `reactive-product-api`
  *   **Packaging:** Jar
  *   **Java:** 21 (or higher)
  *   **Dependencies:**
  *   `Spring Reactive Web` (for WebFlux)
  *   `Spring Data Reactive MongoDB`
  *   `Lombok` (Optional, but highly recommended for boilerplate reduction)

  c.  Generate and download the project. Import it into your IDE.

  #### 3. Define the `Product` Model

  Create a `Product` class in a `edu.miu.cs544.reactiveproductapi.model` package (or similar) with the following fields:

    *   `id`: `String` (Annotate with `@Id` from `org.springframework.data.annotation.Id`)
    *   `name`: `String`
    *   `description`: `String`
    *   `price`: `double`

  Use Lombok annotations (`@Data`, `@NoArgsConstructor`, `@AllArgsConstructor`) for convenience.

  ```java
  // Example structure for Product.java
  import org.springframework.data.annotation.Id;
  import org.springframework.data.mongodb.core.mapping.Document;
  import lombok.AllArgsConstructor;
  import lombok.Data;
  import lombok.NoArgsConstructor;
  
  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Document(collection = "products")
  public class Product {
      @Id
      private String id;
      private String name;
      private String description;
      private double price;
  }
  ```

  #### 4. Create a Reactive Repository

  Create an interface `ProductRepository` that extends `ReactiveMongoRepository<Product, String>`.

  ```java
  // Example structure for ProductRepository.java
  import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
  
  public interface ProductRepository extends ReactiveMongoRepository<Product, String> {
  }
  ```

  #### 5. Implement a Reactive Service Layer

  Create a `ProductService` class. This class should inject `ProductRepository` and provide the following reactive methods:

    *   `Flux<Product> findAllProducts()`: Returns all products.
    *   `Mono<Product> findProductById(String id)`: Returns a product by its ID.
    *   `Mono<Product> saveProduct(Product product)`: Saves a new product.
    *   `Mono<Product> updateProduct(String id, Product product)`: Updates an existing product. Handle cases where the product with the given ID doesn't exist (e.g., return `Mono.empty()`).
    *   `Mono<Void> deleteProduct(String id)`: Deletes a product by its ID.

  Remember to use `Mono` and `Flux` appropriately for all return types.

  ```Java
  @Service
  public class ProductService {
      @Autowired
      private ProductRepository productRepository;
  
      public Flux<Product> finadAllProducts() {
          return productRepository.findAll();
      }
  
      public Mono<Product> findProductById(String id) {
          return productRepository.findById(id);
      }
  
      public Mono<Product> saveProduct(Product product) {
          return productRepository.save(product);
      }
  
      public Mono<Product> updateProduct(String id, Product product) {
          return productRepository.findById(id)
                  .flatMap(existingProduct -> {
                      existingProduct.setName(product.getName());
                      existingProduct.setDescription(product.getDescription());
                      existingProduct.setPrice(product.getPrice());
                      return productRepository.save(existingProduct);
                  });
      }
  
      public Mono<Void> deleteProduct(String id) {
          return productRepository.deleteById(id);
      }
  }
  ```

  #### 6. Create a Reactive REST Controller

  Create a `ProductController` class. This controller should be annotated with `@RestController` and `@RequestMapping("/api/products")`. It should expose the following REST endpoints, using the `ProductService`:

    *   `GET /api/products`: Returns `Flux<Product>` (all products).
    *   `GET /api/products/{id}`: Receives id as an @PathVariable, returns `Mono<ResponseEntity<Product>>` for a specific product ID. Use `.map()` to convert from `Mono<Product>` to `Mono<ResponseEntity<Product>>` and use `.defaultIfEmpty(ResponseEntity.notFound().build())` to indicate 404.
    *   `POST /api/products`: Creates a new product. Expects a `Product` in the request body. Returns `Mono<ResponseEntity<Product>>` with the URI of the created product using `ResponseEntity.created(uri).build()`.
    *   `PUT /api/products/{id}`: Updates an existing product. Expects id as a @PathVariable and a `Product` in the request body. Returns `Mono<ResponseEntity<Product>>` with the updated product. Handle `404 Not Found` if the product doesn't exist (in a similar way to get product).
    *   `DELETE /api/products/{id}`: Deletes a product. Returns `Mono<ResponseEntity<Void>>`. Because the service returns a `Mono<Void>` we cannot use `.map()`, instead use `.then(Mono.just(ResponseEntity.noContent().<Void>)build()))` for when the delete is successful and again `.defaultIfEmpty(ResponseEntity.notFound().build())` for if the id did not exist.

  Ensure proper HTTP status codes are returned for each operation.

  #### 7. Configure MongoDB Connection

  In your `src/main/resources/application.properties` file, add the following configuration:

  ```properties
  spring.data.mongodb.uri=mongodb://localhost:27017/productdb
  ```

  #### 8. Create `App.java`

    * Create an `App.java` class with a main method, annotated with `@SpringBootApplication` and `@EnableReactiveMongoRepositories`

  ```Java
  package edu.miu.cs544;
  
  import org.springframework.boot.SpringApplication;
  import org.springframework.boot.autoconfigure.SpringBootApplication;
  import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;
  
  @SpringBootApplication
  @EnableReactiveMongoRepositories
  public class App {
      public static void main(String[] args) {
          SpringApplication.run(App.class, args);
      }
  }
  ```

  #### 9. Test Your API

  Start your Spring Boot application. Use Postman, Insomnia, or cURL to test your endpoints:

    *   **Create Product (POST):**
        ```bash
        curl -X POST -H "Content-Type: application/json" -d '{"name":"Laptop","description":"High-performance laptop","price":1200.00}' http://localhost:8080/api/products
        ```
    *   **Get All Products (GET):**
        ```bash
        curl http://localhost:8080/api/products
        ```
    *   Make a **screenshot** of your successful GET request to submit as one of the deliverables.

    *   **Get Product by ID (GET):** (Replace `{id}` with an actual ID from a created product)
        ```bash
        curl http://localhost:8080/api/products/{id}
        ```
    *   **Update Product (PUT):** (Replace `{id}` with an actual ID)
        ```bash
        curl -X PUT -H "Content-Type: application/json" -d '{"name":"Laptop Pro","description":"Updated high-performance laptop","price":1350.00}' http://localhost:8080/api/products/{id}
        ```
    *   **Delete Product (DELETE):** (Replace `{id}` with an actual ID)
        ```bash
        curl -X DELETE http://localhost:8080/api/products/{id}
        ```

  #### 10. Check the Database

  After interacting with your API, it's crucial to verify that the data has been persisted correctly in the MongoDB database. You have several options to check the data:

    *   **GUI Application:** Use a MongoDB GUI client like [MongoDB Compass](https://www.mongodb.com/products/compass) or [Robo 3T](https://robomongo.org/download). Connect to `mongodb://localhost:27017/productdb` and browse the `products` collection.

    *   **Command Line (via Docker):** You can connect to the MongoDB instance running inside your Docker container using the `mongosh` client. This allows you to execute MongoDB shell commands directly.

        First, connect to the `my-mongo-db` container's shell:
        ```bash
        docker exec -it my-mongo-db mongosh
        ```

        Once connected to the `mongosh` prompt, you can perform the following checks and direct manipulations:
        1.  Switch to your application's database:
            ```javascript
            use productdb
            ```
        2.  List all collections in the current database:
            ```javascript
            show collections
            ```
            You should see `products` listed.
        3.  Find all documents in the `products` collection:
            ```javascript
            db.products.find({}) 
            ```
            This command will display all the product documents you've added or modified via your API. Make a **screenshot** of this output for submission.

    *  **Direct Database Operations (CRUD via `mongosh`):**
       You can also directly manipulate the database using `mongosh` commands, bypassing your API. This is useful for debugging or data seeding.

        *   **Insert a new product:**
            ```javascript
            db.products.insertOne({
                name: "Keyboard",
                description: "Mechanical RGB keyboard",
                price: 150.00
            });
            db.products.find({ name: "Keyboard" }); // Verify the new entry
            ```

        *   **Update an existing product:**
            *(Note: To update, you'll typically use the `_id` of an existing document. You can find `_id` values from the `db.products.find({})` output. For this example, we'll query by name assuming uniqueness, but `_id` is more robust.)*
            ```javascript
            db.products.updateOne(
                { name: "Laptop" }, // Query to find the document. Use { _id: ObjectId("YOUR_PRODUCT_ID_HERE") } for exact match.
                { $set: { price: 1300.00, description: "Updated high-performance laptop model" } } // Fields to update
            );
            db.products.find({ name: "Laptop" }); // Verify the update for this specific product
            ```

        *   **Delete a product:**
            *(Note: Similar to update, you'll need a query to identify the document to delete.)*
            Let's delete the "Keyboard" we just inserted for demonstration:
            ```javascript
            db.products.deleteOne({ name: "Keyboard" });
            db.products.find({}); // Verify the deletion (it should no longer appear)
            ```
        *   To exit the `mongosh` commandline application press `CTRL-D`

  #### Submission Instructions

  Be sure to delete your target directory, and then compress your entire Spring Boot project directory (the one containing `pom.xml`) into a single `.zip` file. The zip file should contain all necessary source code, configuration files, build scripts, and a `README.md` file that includes instructions on how to run the project, so that the project can be built and run directly by the instructor after unzipping. The `README.md` should specifically mention the Docker command to start MongoDB.

  #### Grading Criteria (Total: 10 points)

    *   **1 point:** Project setup (correct dependencies, project builds successfully).
    *   **2 points:** MongoDB Docker setup and successful connection (application can connect to the Dockerized MongoDB instance running via `docker run` command).
    *   **1 point:** Correct `Product` model definition with `@Id` and `@Document` annotations.
    *   **1 point:** Proper use of `ReactiveMongoRepository` for data access.
    *   **1 point:** `ProductService` implements all required CRUD operations using reactive types (`Mono`, `Flux`) and handles business logic appropriately.
    *   **2 points:** `ProductController` implements all 5 REST endpoints (GET all, GET by ID, POST, PUT, DELETE) with correct HTTP methods, request/response bodies, appropriate HTTP status codes (e.g., 200 OK, 201 Created, 404 Not Found), and reactive return types.
    *   **1 point:** Screenshot of a successful GET request to your API (e.g., `GET /api/products` using Postman or cURL).
    *   **1 point:** Screenshot of the data inside MongoDB, obtained either via a GUI tool or the `docker exec -it my-mongo-db mongosh` command as demonstrated in step 10.