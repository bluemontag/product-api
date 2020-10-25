package com.textplus.productapi;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.math.BigDecimal;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.textplus.productapi.model.Product;

import org.junit.jupiter.api.Test;

public class ProductTests {

    private Logger logger = Logger.getLogger(this.getClass().getName());
    
    @Test
    public void givenAProductWhenCreateThenHasRightPropertiesTest() {
        // Given a code, a name, and a price
        int code = 1;
        String name = "Product A";
        BigDecimal price = new BigDecimal("34.54");

        // When creating a product
        Product p = new Product(code, name, price);

        // Then has the right values
        assertNotNull(p);
        assertEquals(code, p.getCode());
        assertEquals(name, p.getName());
        assertEquals(price, p.getPrice());

        logger.log(Level.INFO, "Test passed.");
    }
}
