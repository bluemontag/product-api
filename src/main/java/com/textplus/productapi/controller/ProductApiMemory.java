package com.textplus.productapi.controller;

import java.io.Closeable;
import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.textplus.productapi.model.Product;

import org.springframework.stereotype.Component;

@Component
public class ProductApiMemory implements Closeable {

    // the product catalog
    private ConcurrentHashMap<Integer, Product> products = new ConcurrentHashMap<>();
    private ReadWriteLock lock = new ReentrantReadWriteLock();
    private ExecutorService executor = Executors.newFixedThreadPool(10);
    

    public CompletableFuture<Optional<Product>> getProductWithCode(Integer code) {
        lock.readLock().lock();

        CompletableFuture<Optional<Product>> result;
        
        if(products.containsKey(code)) {
            result = CompletableFuture.completedFuture(
                Optional.of(products.get(code))
            );
        } else {
            result = CompletableFuture.completedFuture(Optional.empty());
        }

        lock.readLock().unlock();

        return result;
    }

    public CompletableFuture<Boolean> existsInCatalog(Integer code) {
        lock.readLock().lock();

        CompletableFuture<Boolean> result;
        
        if(products.containsKey(code)) {
            result = CompletableFuture.completedFuture(true);
        } else {
            result = CompletableFuture.completedFuture(false);
        }

        lock.readLock().unlock();

        return result;
    }

    /**
     * Returns true iff the product is added,
     *  false if the product was already in the catalog and the operation 
     *  didn't change the catalog collection.
     * 
     * @param p the {@link Product}
     * @return 
     */
    public CompletableFuture<Boolean> addProductToCatalog(Product p) {

        return this.existsInCatalog(p.getCode()).thenApply( (Boolean exists) -> {

            boolean addProduct = !exists.booleanValue();

            if (addProduct) {
                lock.writeLock().lock();
                // add the product
                this.products.put(p.getCode(), p);
                lock.writeLock().unlock();
            }
            return addProduct;
        });
    }

    public CompletableFuture<Collection<Product>> getAllProducts() {
        lock.readLock().lock();

        CompletableFuture<Collection<Product>> result =
            CompletableFuture.completedFuture(products.values());

        lock.readLock().unlock();

        return result;
    }

    public void deleteAllProducts() {
        lock.writeLock().lock();
        products = new ConcurrentHashMap<>();
        lock.writeLock().unlock();
    }

    @Override
    public void close() {
        executor.shutdown();
    }

}
