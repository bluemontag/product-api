package com.textplus.productapi;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.textplus.productapi.model.Order;
import com.textplus.productapi.model.Product;
import com.textplus.productapi.model.ProductInOrder;

public class TestUtils {

    private static Logger logger = Logger.getLogger(TestUtils.class.getName());
    
    private static Random random = new Random();

    public static List<Product> createRandomProductList(int maxLen) {
        List<Product> result = new ArrayList<>();
        int len = random.nextInt(maxLen);
        int i = 0;
        while (i < len) {
            Product p = createRandomProduct();

            // check repetitions
            while (result.contains(p)) {
                p = createRandomProduct();
            }
            result.add(p);
            i++;
        }

        logger.log(Level.INFO, () -> "Product list of length " + len + " created.");

        return result;
    }

    public static Product createRandomProduct() {
        Float price = random.nextFloat() * 100;
        int id = random.nextInt(10000000);
        String name = "Product id=" + id;

        Product p = new Product(id, name, new BigDecimal(price));
        
        logger.log(Level.INFO, () -> "Product created: " + p.toString());

        return p;
    }

    public static void assertOrderHasProducts(Order o, List<Product> products) {
        List<Integer> orderCodes = o.getProducts().stream().map( ProductInOrder::getCode ).collect(Collectors.toList());
        List<Integer> productsCodes = products.stream().map( Product::getCode ).collect(Collectors.toList());
        assertArrayEquals(orderCodes.toArray(), productsCodes.toArray());
    }

    public static void assertOrderHasProductPrices(Order o, List<Product> products) {
        List<BigDecimal> orderCodes = o.getProducts().stream().map( ProductInOrder::getPrice ).collect(Collectors.toList());
        List<BigDecimal> productsCodes = products.stream().map( Product::getPrice ).collect(Collectors.toList());
        assertArrayEquals(orderCodes.toArray(), productsCodes.toArray());
    }

}
