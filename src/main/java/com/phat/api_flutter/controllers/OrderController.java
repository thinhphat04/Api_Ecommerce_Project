package com.phat.api_flutter.controllers;

import com.phat.api_flutter.models.Order;
import com.phat.api_flutter.service.impl.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    ICartService cartService;
    IOrderService orderService;
    IUserServiceAdmin userServiceAdmin;
    IProductService productService;
    ICategoryService categoryService;

    public TransactionTemplate transactionTemplate;

    private static final int MAX_RETRIES = 3;

    @Autowired
    public OrderController(IOrderService orderService,
                           IUserServiceAdmin userServiceAdmin,
                           IProductService productService,
                           ICategoryService categoryService,
                            ICartService cartService) {
        this.orderService = orderService;
        this.userServiceAdmin = userServiceAdmin;
        this.productService = productService;
        this.categoryService = categoryService;
        this.cartService = cartService;
    }

    public void handleConflict(Order orderData, int retries) {
        if (retries < MAX_RETRIES) {
            try {
                // Đợi một giây trước khi thử lại
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            // Tăng số lần thử lại và gọi lại createOrderWithRetry
//            createOrderWithRetry(orderData, retries + 1);
        } else {
            // Nếu đạt tối đa số lần thử lại, báo lỗi
            System.err.println("ORDER CREATION FAILED: Order conflict, please try again later");
        }
    }
}