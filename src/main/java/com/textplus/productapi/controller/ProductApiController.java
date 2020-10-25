package com.textplus.productapi.controller;

import java.util.Collection;

import com.textplus.productapi.model.Product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductApiController {

	@Autowired
	private ProductApiMemory memory;

    @GetMapping("/")
	public String home() {
		return "Welcome to the Product API";
    }
    
    @GetMapping("/products")
	public Collection<Product> getAllProducts() {
		return memory.getAllProducts().join();
	}

	@GetMapping("/product")
	public Boolean addProduct(@RequestParam Integer code, @RequestParam String name, @RequestParam Float price) {
		
		Product p = new Product(code, name, price);

		Boolean result = memory.addProductToCatalog(p).join(); // NOSONAR
		
		return result;
	}

	@DeleteMapping("/deleteProducts")
	public void deleteAllProducts() {
		memory.deleteAllProducts();
	}
}