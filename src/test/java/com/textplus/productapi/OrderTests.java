package com.textplus.productapi;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.textplus.productapi.model.Order;
import com.textplus.productapi.model.Product;

import org.junit.jupiter.api.Test;

public class OrderTests {

    private Logger logger = Logger.getLogger(this.getClass().getName());

    @Test
    public void givenOrderData_WhenCreating_ThenHasRightProperties() {
        // Given a date
        Date purchaseDate = new Date();
        // a buyer's email
        String email = "harry@email.com";
        
        // When creating an order
        Order order = new Order(email, purchaseDate);

        // Then
        // it has the buyer's email
        assertEquals(email, order.getBuyersEmail());
        // it has the same purchase date
        assertEquals(purchaseDate, order.getPurchaseDate());
        
        logger.log(Level.INFO, "Test passed.");
    }

    @Test
    public void givenAnOrder_WhenAddingProducts_ThenHasProductsWithSamePrices() {
        // Given a list of products
        List<Product> products = TestUtils.createRandomProductList(50);
        // and an order
        Order order = new Order("harry@email.com", new Date());

        // When adding products to the order:
        order.addProducts(products);

        // Then it has the same products
        TestUtils.assertOrderHasProducts(order, products);
        // at the same prices
        TestUtils.assertOrderHasProductPrices(order, products);

        logger.log(Level.INFO, "Test passed.");
    }

    @Test
    public void givenARandomOrder_WhenCalculatingTotal_ThenValueIsCorrect() {
        // Given a list of products
        List<Product> products = TestUtils.createRandomProductList(50);
        // and an order
        Order order = new Order("harry@email.com", new Date());

        // When adding products to the order:
        order.addProducts(products);

        // Then total is equal to the products' total
        TestUtils.assertOrderTotalIsTheSumOfAllProducts(order, products);
    }

}
