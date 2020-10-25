package com.textplus.productapi;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.math.BigDecimal;

import com.textplus.productapi.model.Product;
import org.junit.jupiter.api.Test;

public class ProductTests {
    
    @Test
    public void createProductTest() {
        // Given a product
        Product p = new Product(1, "Product A", new BigDecimal("34.54"));

        // When creating
        // Then has the right values
        assertNotNull(p);
        assertEquals(1, p.getCode());
        assertEquals("Product A", p.getName());
        assertEquals(new BigDecimal("34.54"), p.getPrice());
    }
}
