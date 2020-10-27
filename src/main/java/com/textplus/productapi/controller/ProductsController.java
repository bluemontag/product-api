package com.textplus.productapi.controller;

import java.util.Collection;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.textplus.productapi.model.IProductsCatalog;
import com.textplus.productapi.model.Product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value="/productsapi/")
public class ProductsController {

	@Autowired
	private IProductsCatalog productsCatalog;

    @GetMapping("/")
	public String home() {
		return "<h1>Welcome to the Product API</h1>";
    }
    
    @GetMapping("/products")
	public Collection<Product> getAllProducts() {
		return this.productsCatalog.getAllProducts().join();
	}

	/**
	 * Creates a new product
	 */
	@PostMapping("/product")
	public Boolean addProduct(@NotNull @Valid @RequestBody Product p) {
		
		Boolean result = this.productsCatalog.addProductToCatalog(p).join(); // NOSONAR
		
		return result;
	}

	/**
	 * Delete the product with the code "code"
	 * 
	 * @param code the code of the product to remove from the memory
	 * @return Boolean true iff the element is found and removed successfully
	 */
	@DeleteMapping("/product")
	public Boolean deleteProduct(@NotNull Integer code) {
		
		Boolean result = this.productsCatalog.deleteProduct(code).join(); // NOSONAR
		
		return result;
	}
	
	@PutMapping("/product")
	public Boolean updateProduct(@NotNull @Valid @RequestBody Product p) {
		return this.productsCatalog.updateProduct(p).join();
	}

	/**
	 * Deletes all products in the catalog
	 */
	@DeleteMapping("/products")
	public void deleteAllProducts() {
		this.productsCatalog.deleteAllProducts();
	}
}