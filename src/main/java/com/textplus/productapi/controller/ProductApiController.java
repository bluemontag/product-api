package com.textplus.productapi.controller;

import java.util.Collection;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.textplus.productapi.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductApiController {

	@Autowired
	private ProductApiMemory memory;

    @GetMapping("/")
	public String home() {
		return "<h1>Welcome to the Product API</h1>";
    }
    
    @GetMapping("/products")
	public Collection<Product> getAllProducts() {
		return memory.getAllProducts().join();
	}

	@PostMapping("/product")
	public Boolean addProduct(@NotNull @Valid @RequestBody Product p) {
		
		Boolean result = memory.addProductToCatalog(p).join(); // NOSONAR
		
		return result;
	}

	@DeleteMapping("/deleteProducts")
	public void deleteAllProducts() {
		memory.deleteAllProducts();
	}
}