package com.textplus.productapi;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductApiController {

    @GetMapping("/")
	public String home() {
		return "Welcome to the Product API";
    }
    
    @GetMapping("/getProducts")
	public String getProducts() {
		return null;
	}

}