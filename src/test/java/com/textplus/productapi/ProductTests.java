package com.textplus.productapi;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.textplus.productapi.model.Order;
import com.textplus.productapi.model.Product;
import com.textplus.productapi.model.ProductInOrder;

import org.junit.jupiter.api.Test;

public class ProductTests {

    private Logger logger = Logger.getLogger(this.getClass().getName());
    
    private Random random = new Random();

    @Test
    public void givenAProductWhenCreateThenHasRightPropertiesTest() {
        // Given
        
        // When creating
        Product p = new Product(1, "Product A", new BigDecimal("34.54"));

        // Then has the right values
        assertNotNull(p);
        assertEquals(1, p.getCode());
        assertEquals("Product A", p.getName());
        assertEquals(new BigDecimal("34.54"), p.getPrice());
    }

    @Test
    public void givenAListOfProductsWhenCreatingAnOrderThenHasRightPropertiesTest() {

        // Given a list of products
        List<Product> products = this.createRandomProductList();
        // a date
        Date purchaseDate = new Date();
        // a buyer's email
        String email = "harry@email.com";
        // And an order
        Order o = new Order(email, purchaseDate);

        // When adding a list of products to the order
        o.addProducts(products);

        // Then
        // it has the same products
        this.assertItHasTheSameProducts(o, products);
        // at the same prices
        this.assertItHasTheSamePrices(o, products);
        // it has the buyer's email
        assertEquals(email, o.getBuyersEmail());
        // it has the same purchase date
        assertEquals(purchaseDate, o.getPurchaseDate());
    }

    private List<Product> createRandomProductList() {
        List<Product> result = new ArrayList<>();
        int len = random.nextInt(100);
        int i = 0;
        while (i < len) {
            Product p = this.createRandomProduct();

            // check repetitions
            while (result.contains(p)) {
                p = this.createRandomProduct();
            }
            result.add(p);
            i++;
        }

        logger.log(Level.INFO, () -> "Product list of length " + len + " created.");

        return result;
    }

    private Product createRandomProduct() {
        Float price = random.nextFloat() * 100;
        int id = random.nextInt(10000000);
        String name = "Product id=" + id;

        Product p = new Product(id, name, new BigDecimal(price));
        
        logger.log(Level.INFO, () -> "Product created: " + p.toString());

        return p;
    }

    private void assertItHasTheSameProducts(Order o, List<Product> products) {
        List<Integer> orderCodes = o.getProducts().stream().map( ProductInOrder::getCode ).collect(Collectors.toList());
        List<Integer> productsCodes = products.stream().map( Product::getCode ).collect(Collectors.toList());
        assertArrayEquals(orderCodes.toArray(), productsCodes.toArray());
    }

    private void assertItHasTheSamePrices(Order o, List<Product> products) {
        List<BigDecimal> orderCodes = o.getProducts().stream().map( ProductInOrder::getPrice ).collect(Collectors.toList());
        List<BigDecimal> productsCodes = products.stream().map( Product::getPrice ).collect(Collectors.toList());
        assertArrayEquals(orderCodes.toArray(), productsCodes.toArray());
    }
}
