package com.textplus.productapi;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.charset.Charset;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.textplus.productapi.controller.ProductApiController;
import com.textplus.productapi.model.Product;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductApiControllerTests {
    
    private Logger logger = Logger.getLogger(this.getClass().getName());

    @Autowired
    private ProductApiController controller;

    @Autowired
    private MockMvc mockMvc;

    private static MediaType json = new MediaType(MediaType.APPLICATION_JSON, Charset.forName("UTF-8"));

    @BeforeEach
    public void emptyMemory() {
        logger.log(Level.INFO, "=========================================================================");
        logger.log(Level.INFO, "Deleting previous data");
        controller.deleteAllProducts();
        logger.log(Level.INFO, "=========================================================================");
    }

    @Test
    public void givenARandomProduct_WhenAdded_ThenOkStatusIsReturned() throws Exception {

        Product p = TestUtils.createRandomProduct();
        // given an invalid product
        logger.log(Level.INFO, () -> "Testing Product : " + p);

        mockMvc.perform(MockMvcRequestBuilders.post("/product")
            .content(p.getJsonString())
            .contentType(json))
            .andExpect(MockMvcResultMatchers.status().isOk());

        logger.log(Level.INFO, "Test passed.");
    }

    @Test
    public void givenARandomProduct_WhenAdded_ThenControllerReturnsTrue() {

        // Given a Product
        Product p = TestUtils.createRandomProduct();

        // When added
        Boolean added = controller.addProduct(p);

        // Then controller returns true
        assertTrue(added);

        logger.log(Level.INFO, "Test passed.");
    }

    @Test
    public void givenAnExistingProduct_WhenAdded_ThenControllerReturnsFalse() {

        // Given a Product
        Product p = TestUtils.createRandomProduct();

        // When added twice
        controller.addProduct(p);
        Boolean added = controller.addProduct(p);

        // Then controller returns false
        assertFalse(added);

        logger.log(Level.INFO, "Test passed.");
    }

    @Test
    public void givenAListOfNProducts_WhenAdded_ThenGetAllProductsReturnsTheSameList() {
        // Given a list of products
        List<Product> products = TestUtils.createRandomProductList(50);

        // When products are added
        products.forEach( p -> controller.addProduct(p));

        // Then controller has all the products
        List<Product> controllerProds = controller.getAllProducts().stream().collect(Collectors.toList());
        TestUtils.assertContainTheSameProducts(controllerProds, products);

        logger.log(Level.INFO, "Test passed.");
    }

    private static final String nullProduct = "{}";
    private static final String productWithoutName = "{\"code\": 11, \"price\": 45.98}";
    private static final String productWithoutCode = "{\"name\": \"Product B\", \"price\": 45.98}";
    private static final String productWithInvalidCode = "{\"name\": \"Product A\", \"code\" : -22, \"price\": 34.95}";
    private static final String productWithoutPrice = "{\"name\": \"Product A\", \"code\" : 2}";

    @ParameterizedTest
    @ValueSource(strings = {nullProduct,
                            productWithoutName,
                            productWithoutCode,
                            productWithInvalidCode,
                            productWithoutPrice})
    public void givenAnInvalidProduct_WhenAdded_ThenBadRequestIsReturned(String invalidProductJSON) throws Exception {
        // given an invalid product
        logger.log(Level.INFO, () -> "Testing Product JSON: " + invalidProductJSON);
        mockMvc.perform(MockMvcRequestBuilders.post("/product")
            .content(invalidProductJSON)
            .contentType(json))
            .andExpect(MockMvcResultMatchers.status().isBadRequest());

        logger.log(Level.INFO, "Test passed.");
    }

}
