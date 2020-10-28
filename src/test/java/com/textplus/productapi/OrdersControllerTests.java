package com.textplus.productapi;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.textplus.productapi.controller.OrdersController;
import com.textplus.productapi.controller.ProductsController;
import com.textplus.productapi.model.Order;
import com.textplus.productapi.model.Product;
import com.textplus.productapi.model.ProductInOrder;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
public class OrdersControllerTests {

    private Logger logger = Logger.getLogger(this.getClass().getName());

    @Autowired
    private OrdersController controller;

    @Autowired
    private ProductsController productsController;
    
    @Autowired
    private MockMvc mockMvc;

    private static final MediaType MEDIA_TYPE_JSON = new MediaType(MediaType.APPLICATION_JSON, Charset.forName("UTF-8"));

    @BeforeEach
    public void emptyMemory() {
        logger.log(Level.INFO, "=========================================================================");
        logger.log(Level.INFO, "Deleting previous data");
        controller.deleteAllOrders();
        logger.log(Level.INFO, "=========================================================================");
    }

    @Test
    public void whenPlacingAnOrder_ThenOrderIsStored() throws Exception {
        // Given an email and a date
        String myDate = "28/10/2020";
        String myEmail = "ignacio@gmail.com";
        MvcResult result = mockMvc.perform(
                                        MockMvcRequestBuilders.post("/ordersapi/order?purchaseDateStr=" + myDate + "&email=" + myEmail)
                                        .contentType(MEDIA_TYPE_JSON))
                          .andExpect(MockMvcResultMatchers.status().isOk())
                          .andReturn();

        // espero con timeout una respuesta positiva (TRUE)
        String uuid = result.getResponse().getContentAsString();
        assertNotNull(uuid);
        assertNotEquals("", uuid);

        // Then
        // check if order is stored
        result = mockMvc.perform(MockMvcRequestBuilders.get("/ordersapi/order?orderUUID=" + uuid)
                                 .contentType(MEDIA_TYPE_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        
        // read the returning JSON
        Order order = new ObjectMapper().readValue(result.getResponse().getContentAsString(), Order.class);
        
        assertNotNull(order);
        assertEquals(uuid, order.getUuid());
        assertEquals(myEmail, order.getBuyersEmail());
        
        assertEquals(myDate, TestUtils.dateToString(order.getPurchaseDate()));

        logger.log(Level.INFO, "Test passed.");
    }

    @Test
    public void givenAnOrder_WhenAddingAProduct_ThenProductIsInTheOrder() {
        // Given an email and a purchase date:
        String email = "ignacio@gmail.com";
        String purchaseDate = "13/10/2020";

        // When placing an order
        String uuid = this.placeAnOrder(email, purchaseDate);

        // And adding a random product
        Product prod = TestUtils.createRandomProduct();
        Boolean added = controller.addProductToOrder(uuid, prod);

        // Then
        // product was added
        assertTrue(added.booleanValue());

        // order has the product
        Order order = controller.getOrder(uuid).getBody();

        assertNotNull(order);
        assertEquals(1, order.getProducts().size());
        TestUtils.assertOrderHasProduct(order, prod);

        logger.log(Level.INFO, "Test passed.");
    }

    @Test
    public void givenAnOrder_WhenAddingNProducts_ThenOrderTotalIsCorrect() {
        // Given an order
        String email = "ignacio@gmail.com";
        String purchaseDate = "26/10/2020";
        String uuid = this.placeAnOrder(email, purchaseDate);

        // And a list of N products
        List<Product> prods = TestUtils.createRandomProductList(25);

        // When
        // Adding the N products to the order:
        this.addNProducts(uuid, prods);

        // Then
        // The order has the product
        Order order = controller.getOrder(uuid).getBody();

        assertNotNull(order);
        assertEquals(prods.size(), order.getProducts().size());
        TestUtils.assertOrderHasProducts(order, prods);
        TestUtils.assertOrderTotalIsTheSumOfAllProducts(order, prods);

        logger.log(Level.INFO, "Test passed.");
    }

    @Test
    public void givenTwoOrders_WhenSelectingBetweenDates_ThenOneIsCollected() {
        // Given two emails and two purchases dates:
        String email1 = "ignacio@gmail.com";
        String purchaseDate1 = "26/10/2020";

        String email2 = "adrian@gmail.com";
        String purchaseDate2 = "30/10/2020";
        // When placing two orders
        String uuid1 = this.placeAnOrder(email1, purchaseDate1);
        String uuid2 = this.placeAnOrder(email2, purchaseDate2);

        // And selecting between 2 dates (that should select only one order)
        String selectDate1 = "29/10/2020";
        String selectDate2 = "31/10/2020";
        List<Order> orders = (List<Order>)controller.getOrdersBetween(selectDate1, selectDate2).getBody();

        // Then the second is selected
        assertEquals(1, orders.size());
        assertNotEquals(uuid1, orders.get(0).getUuid());
        assertEquals(uuid2, orders.get(0).getUuid());

        logger.log(Level.INFO, "Test passed.");
    }

    @Test
    public void givenTwoOrders_WhenPlacingThem_ThenGetAllBringsTheTwo() {
        // Given two emails and two purchases dates:
        String email1 = "ignacio@gmail.com";
        String purchaseDate1 = "26/10/2020";

        String email2 = "adrian@gmail.com";
        String purchaseDate2 = "30/10/2020";
        // When placing two orders
        String uuid1 = this.placeAnOrder(email1, purchaseDate1);
        String uuid2 = this.placeAnOrder(email2, purchaseDate2);

        // And selecting all orders
        List<Order> orders = (List<Order>)controller.getAllOrders().getBody();

        // Then the collection has size 2
        assertEquals(2, orders.size());
        assertEquals(uuid1, orders.get(0).getUuid());
        assertEquals(uuid2, orders.get(1).getUuid());

        logger.log(Level.INFO, "Test passed.");
    }

    private String placeAnOrder(String email, String purchaseDate) {
        // When placing an order
        String uuid = controller.addOrder(email, purchaseDate).getBody();
        assertNotNull(uuid);
        assertNotEquals("", uuid);
        return uuid;
    }
    
    private void addNProducts(String uuid, List<Product> prods) {
        // And adding N products
        prods.forEach( prod -> {
            Boolean added = controller.addProductToOrder(uuid, prod);
            // product was added
            assertTrue(added.booleanValue());
        });
    }

    @Test
    public void givenAnOrder_WhenUpdatingAProductPrice_ThenProductInOrderDoesNotChange() {
        // Given an order
        String email = "ignacio@gmail.com";
        String purchaseDate = "26/10/2020";
        // When placing order
        String uuid = this.placeAnOrder(email, purchaseDate);

        // And adding a product
        Product p = TestUtils.createRandomProduct();
        BigDecimal originalPrice = p.getPrice();
        Boolean added = controller.addProductToOrder(uuid, p);
        assertTrue(added.booleanValue());

        // When updating product price
        BigDecimal newPrice = originalPrice.add(new BigDecimal(10.00)); // rise in 10 the original price
        p.setPrice(newPrice);
        Boolean updated = productsController.updateProduct(p);
        assertTrue(updated.booleanValue());

        // Then when getting the order:
        Order order = controller.getOrder(uuid).getBody();

        // Then the orders price does not change
        ProductInOrder orderProduct = order.getProducts().get(0);
        assertEquals(originalPrice, orderProduct.getPrice());

        // And the product has the new price
        Product p2 = productsController.getProductWithCode(p.getCode()).getBody();
        assertNotNull(p2);
        assertEquals(newPrice, p2.getPrice());

        logger.log(Level.INFO, "Test passed.");
    }
}
