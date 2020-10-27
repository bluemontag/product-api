package com.textplus.productapi.model;

import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface IOrdersCatalog {
    
    public CompletableFuture<List<Order>> getAllOrders();
    public CompletableFuture<List<Order>> getOrdersBetween(Date beginDate, Date endDate);
    public CompletableFuture<Boolean> addOrderToCatalog(Order order);
    public CompletableFuture<Boolean> orderExistsInCatalog(String orderUUID);
    public CompletableFuture<Boolean> addProductToOrder(String orderUUID, Product prod);
}
