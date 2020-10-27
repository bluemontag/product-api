package com.textplus.productapi.controller;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.textplus.productapi.model.IOrdersCatalog;
import com.textplus.productapi.model.Order;
import com.textplus.productapi.model.Product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value="/ordersapi/")
public class OrdersController {

    @Autowired
    private IOrdersCatalog ordersCatalog;
    
    @GetMapping("/")
	public String home() {
		return "<h1>Welcome to the Orders API</h1>";
    }

    /**
     * Get mapping for querying all the orders between two dates
     * @param beginDateStr Date string in format dd/MM/yyyy
     * @param endDateStr Date string in format dd/MM/yyyy
     * @return
     */
	@GetMapping("/orders")
	public ResponseEntity<Collection<Order>> getAllOrdersBetween(@NotNull String begin, @NotNull String end) {
        Date beginDate;
        Date endDate;
        try {
            beginDate = new SimpleDateFormat("dd/MM/yyyy").parse(begin);
            endDate = new SimpleDateFormat("dd/MM/yyyy").parse(end);
        } catch (Exception e) {
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);
        }

		return new ResponseEntity<>(this.ordersCatalog.getAllOrdersBetween(beginDate, endDate).join(), HttpStatus.OK);
	}

	@PostMapping("/order")
	public Boolean addOrder(@NotNull @Valid @RequestBody Order order) { 
        return this.ordersCatalog.addOrderToCatalog(order).join();
    }
    
    @PutMapping("/addProduct")
	public Boolean addProductToOrder(@NotNull String orderUUID, @Valid @RequestBody Product prod ) {
        return this.ordersCatalog.addProductToOrder(orderUUID, prod).join();
    }
}
