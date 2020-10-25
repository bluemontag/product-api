package com.textplus.productapi;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.textplus.productapi.controller.ProductApiController;
import com.textplus.productapi.model.Product;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ProductApiControllerTests {
    
    private Logger logger = Logger.getLogger(this.getClass().getName());

    @Autowired
    private ProductApiController controller;

    @BeforeEach
    public void emptyMemory() {
        logger.log(Level.INFO, "=========================================================================");
        logger.log(Level.INFO, "Deleting previous data");
        controller.deleteAllProducts();
        logger.log(Level.INFO, "=========================================================================");
    }

    @Test
    public void givenAProductWhenAddedThenControllerReturnsTrue() {

        // Given a Product
        Product p = TestUtils.createRandomProduct();

        // When added
        Boolean added = controller.addProduct(p.getCode(), p.getName(), p.getPrice().floatValue());

        // Then controller returns true
        assertTrue(added);

        logger.log(Level.INFO, "Test passed.");
    }

    @Test
    public void givenAnExistingProductWhenAddedThenControllerReturnsFalse() {

        // Given a Product
        Product p = TestUtils.createRandomProduct();

        // When added twice
        controller.addProduct(p.getCode(), p.getName(), p.getPrice().floatValue());
        Boolean added = controller.addProduct(p.getCode(), p.getName(), p.getPrice().floatValue());

        // Then controller returns false
        assertFalse(added);

        logger.log(Level.INFO, "Test passed.");
    }

    @Test
    public void givenAListOfNProductsWhenAddedThenGetAllProductsReturnsTheSameList() {
        // Given a list of products
        List<Product> products = TestUtils.createRandomProductList(50);

        // When products are added
        products.forEach( p -> controller.addProduct(p.getCode(), p.getName(), p.getPrice().floatValue()));

        // Then controller has all the products
        List<Product> controllerProds = controller.getAllProducts().stream().collect(Collectors.toList());
        TestUtils.assertContainTheSameProducts(controllerProds, products);

        logger.log(Level.INFO, "Test passed.");
    }
}
