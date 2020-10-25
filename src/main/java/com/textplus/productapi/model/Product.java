package com.textplus.productapi.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class Product implements Serializable {
    
    private static final long serialVersionUID = -3943409050833554778L;
    private Integer code;
    private String name;
    private BigDecimal price;

    public Product(Integer code, String name, BigDecimal price) {
        this.code = code;
        this.name = name;
        this.price = price.setScale(2, RoundingMode.HALF_EVEN);
    }

    public Product(Integer code, String name, double price) {
        this(code, name, BigDecimal.valueOf(price));
    }

    public Product(Integer code, String name, float price) {
        this(code, name, BigDecimal.valueOf(price));
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the price
     */
    public BigDecimal getPrice() {
        return price;
    }

    /**
     * @param price the price to set
     */
    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    /**
     * @return the code
     */
    public Integer getCode() {
        return code;
    }

    /**
     * @param code the code to set
     */
    public void setCode(Integer code) {
        this.code = code;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + code;
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
        Product other = (Product) obj;
        return (code.equals(other.code));
    }

    @Override
    public String toString() {
        return "Product [code=" + code + ", name=" + name + ", price=" + price + "]";
    }

}
