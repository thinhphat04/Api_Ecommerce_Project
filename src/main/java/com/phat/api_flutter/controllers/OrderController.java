package com.phat.api_flutter.controllers;

import com.phat.api_flutter.models.CartProduct;
import com.phat.api_flutter.models.Order;
import com.phat.api_flutter.models.OrderItem;
import com.phat.api_flutter.models.User;
import com.phat.api_flutter.service.CartProductService;
import com.phat.api_flutter.service.ProductService;
import com.phat.api_flutter.service.UserService;
import com.phat.api_flutter.service.impl.*;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    ICartProductService cartProductService;
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
                           ICartProductService cartProductService) {
        this.orderService = orderService;
        this.userServiceAdmin = userServiceAdmin;
        this.productService = productService;
        this.categoryService = categoryService;
        this.cartProductService = cartProductService;
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