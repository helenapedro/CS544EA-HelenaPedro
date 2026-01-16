package edu.miu.cs544.reactiveproductapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@SpringBootApplication
@EnableReactiveMongoRepositories
public class ReactiveProductApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReactiveProductApiApplication.class, args);
	}

}
