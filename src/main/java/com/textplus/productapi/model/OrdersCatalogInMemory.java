package com.textplus.productapi.model;

import java.io.Closeable;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

/**
 * Orders in memory Catalog 
 */
public class OrdersCatalogInMemory implements Closeable, IOrdersCatalog {
   
    private ReadWriteLock ordersLock = new ReentrantReadWriteLock();
    private ConcurrentHashMap<String, Order> orders = new ConcurrentHashMap<>();
    private ExecutorService executor = Executors.newFixedThreadPool(10);
    
    public CompletableFuture<List<Order>> getAllOrders() {

        ordersLock.readLock().lock();
        CompletableFuture<List<Order>> futureList = CompletableFuture.completedFuture(
            this.orders.values().stream().collect(Collectors.toList())
        );
        ordersLock.readLock().unlock();

        return futureList.thenApply( (List<Order> filteredOrders) -> {
            Collections.sort(filteredOrders);
            return filteredOrders;
        });
    }

    public CompletableFuture<Boolean> orderExistsInCatalog(String orderUUID) {
        ordersLock.readLock().lock();

        CompletableFuture<Boolean> result;
        
        if(orders.containsKey(orderUUID)) {
            result = CompletableFuture.completedFuture(true);
        } else {
            result = CompletableFuture.completedFuture(false);
        }

        ordersLock.readLock().unlock();

        return result;
    }

    /**
     * Returns true iff the order is added,
     *  false if the order was already in the catalog and the operation 
     *  didn't change the catalog collection.
     * 
     * @param order the {@link Product}
     * @return 
     */
    public CompletableFuture<Boolean> addOrderToCatalog(Order order) {

        return this.orderExistsInCatalog(order.getOrdersUUIDString()).thenApply( (Boolean exists) -> {

            boolean addOrder = !exists.booleanValue();

            if (addOrder) {
                ordersLock.writeLock().lock();
                // add the order
                this.orders.put(order.getOrdersUUIDString(), order);
                ordersLock.writeLock().unlock();
            }
            return addOrder;
        });
    }

    public CompletableFuture<List<Order>> getOrdersBetween(Date beginDate, Date endDate) {

        ordersLock.readLock().lock();
        CompletableFuture<List<Order>> futureList = CompletableFuture.completedFuture(
            this.orders.values().stream().filter( order -> order.getPurchaseDate().after(beginDate) &&
                                                           order.getPurchaseDate().before(endDate)).collect(Collectors.toList())
        );
        ordersLock.readLock().unlock();

        return futureList.thenApply( (List<Order> filteredOrders) -> {
            Collections.sort(filteredOrders);
            return filteredOrders;
        });
    }

    public CompletableFuture<Boolean> addProductToOrder(String orderUUID, Product prod) {
        return this.orderExistsInCatalog(orderUUID).thenApply( (Boolean exists) -> {

            boolean addProduct = exists.booleanValue();

            if (addProduct) {
                ordersLock.writeLock().lock();
                
                Order order = this.orders.getOrDefault(orderUUID, null);

                // if the order exists
                if (order != null) {
                    order.addProduct(prod);
                    this.orders.put(orderUUID, order);
                } else {
                    addProduct = false;
                }
                
                ordersLock.writeLock().unlock();
            }
            return addProduct;
        });
    }

    @Override
    public void close() {
        executor.shutdown();
    }
}
