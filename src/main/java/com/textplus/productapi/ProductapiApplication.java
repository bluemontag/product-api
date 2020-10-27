package com.textplus.productapi;

import com.textplus.productapi.model.IOrdersCatalog;
import com.textplus.productapi.model.OrdersCatalogInMemory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ProductApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProductApiApplication.class, args);
	}

	@Bean
	public IOrdersCatalog ordersCatalog() {
		return new OrdersCatalogInMemory();
	}
}
