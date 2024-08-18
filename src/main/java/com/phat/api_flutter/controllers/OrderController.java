package com.phat.api_flutter.controllers;

import com.phat.api_flutter.dto.OrderItemRequest;
import com.phat.api_flutter.dto.OrderRequest;
import com.phat.api_flutter.dto.OrderResponseDTO;
import com.phat.api_flutter.dto.OrdersResponseDTO;
import com.phat.api_flutter.models.*;
import com.phat.api_flutter.service.OrderItemService;
import com.phat.api_flutter.service.impl.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@RestController
@RequestMapping("/api/v1/orders/")
public class OrderController {

    IOrderItemSerVice orderItemService;
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
                           ICartService cartService, OrderItemService orderItemService) {
        this.orderService = orderService;
        this.userServiceAdmin = userServiceAdmin;
        this.productService = productService;
        this.categoryService = categoryService;
        this.cartService = cartService;
        this.orderItemService = orderItemService;
    }
    @PostMapping("")
    @Transactional
    public ResponseEntity<?> addOrder(@RequestBody OrderRequest orderRequest) {
        return createOrderWithRetry(orderRequest, 0);
    }

    private ResponseEntity<?> createOrderWithRetry(OrderRequest orderRequest, int retries) {
        try {
            if (retries >= MAX_RETRIES) {
                return ResponseEntity.status(500).body("ORDER CREATION FAILED: Order conflict, please try again later");
            }

            // Kiểm tra user
            User user = userServiceAdmin.findById(orderRequest.getUser());
            if (user == null) {
                return ResponseEntity.status(400).body("ORDER CREATION FAILED: User not found");
            }
            List<OrderItem> orderItems = new ArrayList<>();
            for (OrderItemRequest itemRequest : orderRequest.getOrderItems()) {

                // Kiểm tra sản phẩm
                Optional<Product> productOpt = productService.getProductById(itemRequest.getProduct());
                if (productOpt.isEmpty()) {
                    return ResponseEntity.status(400).body("ORDER CREATION FAILED: Invalid product in the order");
                }
                Product product = productOpt.get();

                // Kiểm tra CartProduct
                CartProduct cartProduct = cartService.findById(itemRequest.getCartProductId());
                if (cartProduct == null) {
                    return ResponseEntity.status(400).body("ORDER CREATION FAILED: Invalid cartProduct in the order");
                }


                OrderItem orderItem = new OrderItem(
                        product,
                        itemRequest.getProductName(),
                        itemRequest.getProductImage(),
                        itemRequest.getProductPrice(),
                        itemRequest.getQuantity(),
                        itemRequest.getSelectedSize(),
                        itemRequest.getSelectedColour()
                );

                orderItemService.AddOrderItem(orderItem);

                if (!cartProduct.isReserved()) {
                    product.setCountInStock(product.getCountInStock() - orderItem.getQuantity());
                    productService.updateProduct(product);
                }
                orderItems.add(orderItem);

                // Xóa CartProduct sau khi xử lý orderItem
                cartService.deleteCartProduct(cartProduct);
                user.getCart().remove(cartProduct.getId());
                userServiceAdmin.updateUser(user);
            }

            Order order = new Order();
            order.setOrderItems(orderItems);
            order.setShippingAddress1(orderRequest.getShippingAddress1());
            order.setCity(orderRequest.getCity());
            order.setPostalCode(orderRequest.getPostalCode());
            order.setCountry(orderRequest.getCountry());
            order.setPhone(orderRequest.getPhone());
            order.setPaymentId(orderRequest.getPaymentId());
            order.setStatus(Order.Status.processed);
            order.setStatusHistory(List.of(Order.Status.pending, Order.Status.processed));
            order.setTotalPrice(orderRequest.getTotalPrice());
            order.setUser(user);
            orderService.addOrder(order);

            OrderResponseDTO orderResponse = new OrderResponseDTO();
            return ResponseEntity.ok(orderResponse.getOrderResponse(order));

        } catch (Exception e) {
            // Xử lý lỗi và thử lại
            return createOrderWithRetry(orderRequest, retries + 1);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderById(@PathVariable String id) {
        try {
            Order order = orderService.getOrderById(id);
            if(order == null) {
                return ResponseEntity.status(404).body("Order not found");
            }
            return ResponseEntity.ok(order);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }
    @GetMapping("/user/{id}")
    public ResponseEntity<OrdersResponseDTO> getUserOrders(@PathVariable String id) {
        try {
            OrdersResponseDTO ordersResponseDTO = orderService.showOrderByUserId(id);
            return ResponseEntity.ok(ordersResponseDTO);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public static class ResourceNotFoundException extends RuntimeException {
        public ResourceNotFoundException(String message) {
            super(message);
        }
    }
}