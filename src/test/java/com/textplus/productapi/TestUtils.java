package com.textplus.productapi;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.textplus.productapi.controller.OrdersController;
import com.textplus.productapi.model.Order;
import com.textplus.productapi.model.Product;
import com.textplus.productapi.model.ProductInOrder;

public class TestUtils {

    private static Logger logger = Logger.getLogger(TestUtils.class.getName());

    private static Random random = new Random();

    public static Order createARandomOrder(int size) {

        Order order = new Order("email@domain.com", new Date());
        List<Product> products = createRandomProductList(size);

        order.addProducts(products);

        return order;
    }

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
        List<Integer> orderCodes = o.getProducts().stream().map(ProductInOrder::getCode).collect(Collectors.toList());
        List<Integer> productsCodes = products.stream().map(Product::getCode).collect(Collectors.toList());
        assertArrayEquals(orderCodes.toArray(), productsCodes.toArray());
    }

    public static void assertOrderHasProductPrices(Order o, List<Product> products) {
        List<BigDecimal> orderCodes = o.getProducts().stream().map(ProductInOrder::getPrice)
                .collect(Collectors.toList());
        List<BigDecimal> productsCodes = products.stream().map(Product::getPrice).collect(Collectors.toList());
        assertArrayEquals(orderCodes.toArray(), productsCodes.toArray());
    }

    public static void assertContainTheSameProducts(List<Product> list1, List<Product> list2) {
        assertEquals(list1.size(), list2.size());
        // for each product in the first list, check if it exists in the second
        list1.forEach(prod -> {
            assertTrue(list2.contains(prod));
        });
    }

    public static void assertOrderTotalIsTheSumOfAllProducts(Order order, List<Product> products) {
        Stream<BigDecimal> productsPrices = products.stream().map(Product::getPrice);
        BigDecimal productsTotal = productsPrices.reduce(new BigDecimal("0"), (v1, v2) -> v1.add(v2));

        BigDecimal orderTotal = order.getTotal();

        assertEquals(orderTotal, productsTotal);
    }

    public static String dateToString(Date date) {
        DateFormat df = new SimpleDateFormat(OrdersController.SPANISH_DATE_FORMAT);
        return df.format(date);
    }

    public static Date stringToDate(String dateStr) throws ParseException {
        return new SimpleDateFormat(OrdersController.SPANISH_DATE_FORMAT).parse(dateStr);
    }

    public static void assertOrderHasProduct(Order o, Product prod) {
        assertNotNull(o);
        assertNotNull(o.getProducts());
        assertTrue(o.getProducts().stream().anyMatch( prodInOrder -> prodInOrder.getCode() == prod.getCode()));
    }
}
