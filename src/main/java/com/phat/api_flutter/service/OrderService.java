package com.phat.api_flutter.service;

import com.phat.api_flutter.controllers.OrderController;
import com.phat.api_flutter.dto.OrderDTO;
import com.phat.api_flutter.dto.OrderResponseDTO;
import com.phat.api_flutter.dto.OrdersResponseDTO;
import com.phat.api_flutter.models.Order;
import com.phat.api_flutter.repository.OrderRepository;
import com.phat.api_flutter.service.impl.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService implements IOrderService {
    @Autowired
    private OrderRepository orderRepository;


    @Override
    public Order addOrder(Order order) {
        return orderRepository.save(order);
    }

    @Override
    public OrdersResponseDTO  showOrderByUserId(String userId) {
        List<Order> orders = orderRepository.findByUserId(userId);
        if (orders == null || orders.isEmpty()) {
            throw new OrderController.ResourceNotFoundException("Orders not found for user id: " + userId);
        }

        List<OrderDTO> completed = new ArrayList<>();
        List<OrderDTO> active = new ArrayList<>();
        List<OrderDTO> cancelled = new ArrayList<>();

        for (Order order : orders) {
            OrderDTO orderDTO = new OrderDTO(order);
            if (order.getStatus() == Order.Status.delivered) {
                completed.add(orderDTO);
            } else if (order.getStatus() == Order.Status.cancelled || order.getStatus() == Order.Status.expired) {
                cancelled.add(orderDTO);
            } else {
                active.add(orderDTO);
            }
        }

        return new OrdersResponseDTO(active, completed, cancelled);

    }

    @Override
    public Order OrderDetails(String orderId) {
        return null;
    }

    @Override
    public boolean DeleteOrderByCusID(String CusID) {
        return false;
    }

    @Override
    public List<Order> ShowAllOrder() {
        return orderRepository.findAll();
    }

    @Override
    public Order UpdateOrder(Order order) {
        Order existingOrder = orderRepository.findById(order.getId()).orElse(null);
        if (existingOrder != null) {
            existingOrder.setId(order.getId());
          existingOrder.setOrderItems(order.getOrderItems());
            existingOrder.setShippingAddress1(order.getShippingAddress1());
            existingOrder.setCity(order.getCity());
            existingOrder.setPostalCode(order.getPostalCode());
            existingOrder.setCountry(order.getCountry());
            existingOrder.setPhone(order.getPhone());
            existingOrder.setPaymentId(order.getPaymentId());
            existingOrder.setStatus(order.getStatus());
            existingOrder.setStatusHistory(order.getStatusHistory());
            existingOrder.setTotalPrice(order.getTotalPrice());
            existingOrder.setUser(order.getUser());
            existingOrder.setDateOrdered(order.getDateOrdered());
            return orderRepository.save(existingOrder);
        }
      return null;
    }

    @Override
    public Order getOrderById(String orderId) {
        return orderRepository.findById(orderId).orElse(null);

    }

    @Override
    public long orderCount() {
        return orderRepository.count();
    }

    @Override
    public List<Order> findByUserId(String id) {
       List<Order> orders = orderRepository.findByUserId(id);
        if (orders == null || orders.isEmpty()) {
           return null;
        }
        return orders;
    }
    @Override
    public boolean deleteOrderById(String orderId) {
        Order order = orderRepository.findById(orderId).orElse(null);
        if (order == null) {
           return false ;
        }
        orderRepository.delete(order);
        return true;
    }


}
