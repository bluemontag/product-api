package com.textplus.productapi.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

public class Order implements Comparable<Order> {

    @NotNull
    private UUID uuid;
    @NotNull
    @Email(message = "The buyers' email is not valid.")
    private String buyersEmail;
    @NotNull
    private Date purchaseDate;
    @NotNull
    private List<ProductInOrder> products;
    @NotNull
    private BigDecimal total;
    
    public Order(String email, Date purchaseDate) {
        this.buyersEmail = email;
        this.purchaseDate = purchaseDate;
        this.products = new ArrayList<>();
        this.total = new BigDecimal("0");
        this.uuid = UUID.randomUUID();
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

    public synchronized void addProduct(Product prod) {

        if (prod == null) {
            throw new IllegalArgumentException("Product to add is null");
        }

        // create product in order (freezes the price)
        ProductInOrder prodInOrder = new ProductInOrder(prod.getCode(), prod.getPrice());

        // add the product
        this.products.add(prodInOrder);

        this.calculateNewTotal();
    }

    private void calculateNewTotal() {
        Stream<BigDecimal> prices = this.products.stream().map( ProductInOrder::getPrice );
        BigDecimal newTotal = prices.reduce(new BigDecimal("0.00") , (v1, v2) -> v1.add(v2) );
        this.total = newTotal.setScale(2, RoundingMode.HALF_EVEN);
    }

    public void addProducts(List<Product> products) {

        if (products == null) {
            throw new IllegalArgumentException("List of products to add is null");
        }
        products.forEach( this::addProduct );
    }

    public BigDecimal getOrderTotal() {
        return this.total;
    }

    public String getOrdersUUIDString() {
        return this.uuid.toString();
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

    @Override
    public int compareTo(Order o) {
        if (o == null) {
            return -1;
        } else if (this.equals(o)) {
            return 0;
        }
        if (this.purchaseDate.before(o.purchaseDate)) {
            return -1;
        } else {
            return 1;
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((uuid == null) ? 0 : uuid.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Order other = (Order) obj;
        if (uuid == null) {
            if (other.uuid != null)
                return false;
        } else if (!uuid.equals(other.uuid))
            return false;
        return true;
    }

}
