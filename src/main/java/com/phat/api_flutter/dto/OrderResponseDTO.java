package com.phat.api_flutter.dto;

import com.phat.api_flutter.models.Order;
import com.phat.api_flutter.models.OrderItem;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Data
public class OrderResponseDTO {
    private String id;
    private List<String> orderItems;
    private String shippingAddress1;
    private String city;
    private String postalCode;
    private String country;
    private String phone;
    private String paymentId;
    private Order.Status status;
    private List<Order.Status> statusHistory;
    private double totalPrice;
    private String userId;
    private Date dateOrdered;

    public OrderResponseDTO getOrderResponse(Order order) {
        List<String> orderItemIds = new ArrayList<>();
        for (OrderItem item : order.getOrderItems()) {
            orderItemIds.add(item.getId());
        }
        OrderResponseDTO orderResponse = new OrderResponseDTO();
        orderResponse.setId(order.getId());
        orderResponse.setOrderItems(orderItemIds);
        orderResponse.setShippingAddress1(order.getShippingAddress1());
        orderResponse.setCity(order.getCity());
        orderResponse.setPostalCode(order.getPostalCode());
        orderResponse.setCountry(order.getCountry());
        orderResponse.setPhone(order.getPhone());
        orderResponse.setPaymentId(order.getPaymentId());
        orderResponse.setStatus(order.getStatus());
        orderResponse.setStatusHistory(order.getStatusHistory());
        orderResponse.setTotalPrice(order.getTotalPrice());
        orderResponse.setUserId(order.getUser().getId());
        orderResponse.setDateOrdered(order.getDateOrdered());

        return orderResponse;
    }

    public OrderResponseDTO getOrderResponse(Order order, String userId) {
        List<String> orderItemIds = new ArrayList<>();
        for (OrderItem item : order.getOrderItems()) {
            orderItemIds.add(item.getId());
        }
        OrderResponseDTO orderResponse = new OrderResponseDTO();
        orderResponse.setId(order.getId());
        orderResponse.setOrderItems(orderItemIds);
        orderResponse.setShippingAddress1(order.getShippingAddress1());
        orderResponse.setCity(order.getCity());
        orderResponse.setPostalCode(order.getPostalCode());
        orderResponse.setCountry(order.getCountry());
        orderResponse.setPhone(order.getPhone());
        orderResponse.setPaymentId(order.getPaymentId());
        orderResponse.setStatus(order.getStatus());
        orderResponse.setStatusHistory(order.getStatusHistory());
        orderResponse.setTotalPrice(order.getTotalPrice());
        orderResponse.setUserId(userId);
        orderResponse.setDateOrdered(order.getDateOrdered());

        return orderResponse;
    }
}


