package com.kata3.kata3;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Kata3Application {

	public static void main(String[] args) {

		//System.out.println("MongoDB URI: " + System.getenv("SPRING_DATA_MONGODB_URI"));
		SpringApplication.run(Kata3Application.class, args);
	}

}
