package com.textplus.productapi.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class Order {
 
    private String buyersEmail;
    private Date purchaseDate;
    private List<ProductInOrder> products;

    public Order(String email, Date purchaseDate) {
        this.buyersEmail = email;
        this.purchaseDate = purchaseDate;
        this.products = new ArrayList<>();
    }

    /**
     * @return the buyersEmail
     */
    public String getBuyersEmail() {
        return buyersEmail;
    }

    /**
     * @param buyersEmail the buyersEmail to set
     */
    public void setBuyersEmail(String buyersEmail) {
        this.buyersEmail = buyersEmail;
    }

    /**
     * @return the purchaseDate
     */
    public Date getPurchaseDate() {
        return purchaseDate;
    }

    /**
     * @param purchaseDate the purchaseDate to set
     */
    public void setPurchaseDate(Date purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public void addProducts(List<Product> products) {

        List<ProductInOrder> prodsInOrder =
          products.stream().map( prod -> new ProductInOrder(prod.getCode(), prod.getPrice())).collect(Collectors.toList());

        this.products = prodsInOrder;
    }

    @Override
    public String toString() {
        return "Order [buyersEmail=" + buyersEmail + ", products=" + products + ", purchaseDate=" + purchaseDate + "]";
    }

    /**
     * @return the products
     */
    public List<ProductInOrder> getProducts() {
        return products;
    }

}
