package com.textplus.productapi.controller;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

import com.textplus.productapi.model.IOrdersCatalog;
import com.textplus.productapi.model.IProductsCatalog;
import com.textplus.productapi.model.Order;
import com.textplus.productapi.model.Product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value="/ordersapi/")
@Validated
public class OrdersController {

    private Logger logger = Logger.getLogger(this.getClass().getName());

    public static final String SPANISH_DATE_FORMAT = "dd/MM/yyyy";

    @Autowired
    private IOrdersCatalog ordersCatalog;

    @Autowired
    private IProductsCatalog productsCatalog;

    @GetMapping("/")
	public String home() {
        logger.log(Level.INFO, () -> "GET /ordersapi/ called.");
		return "<h1>Welcome to the Orders API</h1>";
    }

    @GetMapping("/orders")
	public ResponseEntity<Collection<Order>> getAllOrders() {
        logger.log(Level.INFO, () -> "GET /ordersapi/orders called.");
        Collection<Order> result = this.ordersCatalog.getAllOrders().join();
        logger.log(Level.INFO, () -> "return status code: 200 (OK), with list: " + result);
		return new ResponseEntity<>(result, HttpStatus.OK);
    }
    /**
     * Get mapping for querying all the orders between two dates
     * @param beginDateStr Date string in format dd/MM/yyyy
     * @param endDateStr Date string in format dd/MM/yyyy
     * @return
     */
	@GetMapping("/ordersbetween")
	public ResponseEntity<Collection<Order>> getOrdersBetween(@NotNull String begin, @NotNull String end) {
        logger.log(Level.INFO, () -> "GET /ordersapi/ordersbetween called with begin: \"" + begin + "\" and end: \"" + end + "\"");
        Date beginDate;
        Date endDate;
        try {
            beginDate = new SimpleDateFormat(SPANISH_DATE_FORMAT).parse(begin);
            endDate = new SimpleDateFormat(SPANISH_DATE_FORMAT).parse(end);
        } catch (Exception e) {
            logger.log(Level.INFO, () -> "return empty list with status code: 400 (Bad Request).");
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);
        }
        Collection<Order> result = this.ordersCatalog.getOrdersBetween(beginDate, endDate).join();
        logger.log(Level.INFO, () -> "return status code: 200 (OK), with list: " + result);
		return new ResponseEntity<>(result, HttpStatus.OK);
    }
    
    @GetMapping("/order")
    public ResponseEntity<Order> getOrder(@NotNull String orderUUID) {
        logger.log(Level.INFO, () -> "GET /ordersapi/order called with orderUUID: \"" + orderUUID + "\"");

        Order result = this.ordersCatalog.getOrder(orderUUID).join();

        if (result == null) {
            logger.log(Level.INFO, () -> "Order not found. Return status code 404 (NOT FOUND).");
            return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
        } else {
            logger.log(Level.INFO, () -> "return status code: 200 (OK), with order: " + result);
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
    }

	@PostMapping("/order")
    public ResponseEntity<String> addOrder(@NotNull @Email String email, @NotNull String purchaseDateStr) {
        logger.log(Level.INFO, () -> "POST /ordersapi/order called with email: " + email + " and purchase date: " + purchaseDateStr);
        Date purchaseDate;
        try {
            purchaseDate = new SimpleDateFormat(SPANISH_DATE_FORMAT).parse(purchaseDateStr);
        } catch (Exception e) {
            logger.log(Level.INFO, () -> "return status code: 400 (Bad Request).");
            return new ResponseEntity<>("", HttpStatus.BAD_REQUEST);
        }
        // create order to generate UUID:
        Order order = new Order(email, purchaseDate);
        logger.log(Level.INFO, () -> "Order created: " + order.toString());
        Boolean result = this.ordersCatalog.addOrderToCatalog(order).join();
        logger.log(Level.INFO, () -> "return boolean: " + result); // NOSONAR
        return new ResponseEntity<>(order.getUuid(), HttpStatus.OK);
    }
    
    @PutMapping("/product")
	public Boolean addProductToOrder(@NotNull String orderUUID, @Valid @RequestBody Product prod ) {
        logger.log(Level.INFO, () -> "PUT /ordersapi/product called with orderUUID: " + orderUUID + " and product: " + prod.toString());

        Boolean productExists = this.productsCatalog.productExistsInCatalog(prod.getCode()).join();
        
        final Boolean result;
        final Boolean firstStageOK;
        final Boolean addProductToOrder;
        if (productExists.booleanValue()) {
            firstStageOK = Boolean.TRUE;
        } else {
            firstStageOK = this.productsCatalog.addProductToCatalog(prod).join();
        }
        if (firstStageOK.booleanValue()) {
            addProductToOrder = this.ordersCatalog.addProductToOrder(orderUUID, prod).join();
        } else {
            addProductToOrder = Boolean.FALSE;
        }
        
        result = firstStageOK.booleanValue() && addProductToOrder.booleanValue();
        logger.log(Level.INFO, () -> "return boolean: " + result);
        return result;
    }

    @DeleteMapping("/order")
    public Boolean deleteOrder(@NotNull String orderUUID) {
        logger.log(Level.INFO, () -> "DELETE /ordersapi/order called with UUID: " + orderUUID);
        Boolean result = this.ordersCatalog.deleteOrder(orderUUID).join();
        logger.log(Level.INFO, () -> "return boolean: " + result);
        return result;
    }

    @DeleteMapping("/orders")
    public void deleteAllOrders() {
        logger.log(Level.INFO, () -> "DELETE /ordersapi/orders called");
        this.ordersCatalog.deleteAllOrders();
        logger.log(Level.INFO, () -> "return ok");
    }
}
