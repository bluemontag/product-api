package com.textplus.productapi.model;

import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public interface IProductsCatalog {
 
    
    public CompletableFuture<Collection<Product>> getAllProducts();
    public CompletableFuture<Optional<Product>> getProductWithCode(Integer code);
    public CompletableFuture<Boolean> productExistsInCatalog(Integer code);
    public CompletableFuture<Boolean> addProductToCatalog(Product p);
    public CompletableFuture<Boolean> updateProduct(Product p);
    public CompletableFuture<Boolean> deleteProduct(Integer code);
    public void deleteAllProducts();
    
}

