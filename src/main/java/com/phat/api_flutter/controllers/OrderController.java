package com.phat.api_flutter.controllers;

import com.phat.api_flutter.models.Order;
import com.phat.api_flutter.service.impl.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    IOrderService orderService;
//
//    @Autowired
//    public OrderController(IOrderService orderService) {
//        this.orderService = orderService;
//    }

    @PostMapping
    public String addOrder(@RequestBody Order order) {
        try {
            Order newOrder = orderService.AddOrder(order);
            return "Order added successfully: " + newOrder.getId();
        } catch (RuntimeException ex) {
            return "Error: " + ex.getMessage();
        }
    }

    @DeleteMapping("/customer/{customerId}")
    public String deleteOrderByCustomerId(@PathVariable String customerId) {
        boolean isDeleted = orderService.DeleteOrderByCusID(customerId);
        if (isDeleted) {
            return "Orders deleted successfully for customer ID: " + customerId;
        } else {
            return "Error: Orders not found for customer ID: " + customerId;
        }
    }

    @GetMapping("/{orderId}")
    public String getOrderDetails(@PathVariable String orderId) {
        Order order = orderService.OrderDetails(orderId);
        if (order != null) {
            return "Order details: " + order.toString();
        } else {
            return "Error: Order not found with ID: " + orderId;
        }
    }

    @GetMapping("/user/{userId}")
    public List<Order> showOrderByUserId(@PathVariable String userId) {
        return orderService.ShowOrderByUserId(userId);
    }

    @GetMapping
    public List<Order> getAllOrders() {
        return orderService.ShowAllOrder();
    }

    @GetMapping("/one/{orderId}")
    public String getOneOrder(@PathVariable String orderId) {
        Order order = orderService.GetOneOrder(orderId);
        if (order != null) {
            return "Order details: " + order.toString();
        } else {
            return "Error: Order not found with ID: " + orderId;
        }
    }

    @PutMapping
    public String updateOrder(@RequestBody Order order) {
        Order updatedOrder = orderService.UpdateOrder(order);
        return "Order updated successfully: " + updatedOrder.getId();
    }

//    @DeleteMapping("/product/{productId}")
//    public String deleteOrderDetailByProductId(@PathVariable String productId) {
//        boolean isDeleted = orderService.deleteOrderDetailByProductId(productId);
//        if (isDeleted) {
//            return "Order details deleted successfully for product ID: " + productId;
//        } else {
//            return "Error: Order details not found for product ID: " + productId;
//        }
//    }
}
