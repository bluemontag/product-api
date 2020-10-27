package com.textplus.productapi.controller;

import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.textplus.productapi.model.IProductsCatalog;
import com.textplus.productapi.model.Product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value="/productsapi/")
@Validated
public class ProductsController {

	private Logger logger = Logger.getLogger(this.getClass().getName());

	@Autowired
	private IProductsCatalog productsCatalog;

    @GetMapping("/")
	public String home() {
		logger.log(Level.INFO, () -> "GET /productsapi/ called.");
		return "<h1>Welcome to the Product API</h1>";
    }
    
    @GetMapping("/products")
	public Collection<Product> getAllProducts() {
		logger.log(Level.INFO, () -> "GET /productsapi/products called.");
		Collection<Product> result = this.productsCatalog.getAllProducts().join();
		logger.log(Level.INFO, () -> "return list: " + result);
		return result;
	}

	/**
	 * Creates a new product
	 */
	@PostMapping("/product")
	public Boolean addProduct(@NotNull @Valid @RequestBody Product p) {
		logger.log(Level.INFO, () -> "POST /productsapi/product called with product: " + p.toString());
		Boolean result = this.productsCatalog.addProductToCatalog(p).join();
		logger.log(Level.INFO, () -> "return boolean: " + result); // NOSONAR
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
		logger.log(Level.INFO, () -> "DELETE /productsapi/product called with code: " + code);
		Boolean result = this.productsCatalog.deleteProduct(code).join(); // NOSONAR
		logger.log(Level.INFO, () -> "return boolean: " + result);
		return result;
	}
	
	@PutMapping("/product")
	public Boolean updateProduct(@NotNull @Valid @RequestBody Product p) {
		logger.log(Level.INFO, () -> "PUT /productsapi/product called with product: " + p.toString());
		Boolean result = this.productsCatalog.updateProduct(p).join();
		logger.log(Level.INFO, () -> "return boolean: " + result);
		return result;
	}

	/**
	 * Deletes all products in the catalog
	 */
	@DeleteMapping("/products")
	public void deleteAllProducts() {
		logger.log(Level.INFO, () -> "DELETE /productsapi/products called.");
		this.productsCatalog.deleteAllProducts();
		logger.log(Level.INFO, () -> "deleteAllProducts() finished without exceptions.");
	}
}