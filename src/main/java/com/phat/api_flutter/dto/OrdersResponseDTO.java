package com.phat.api_flutter.dto;

import lombok.Data;

import java.util.List;

@Data
public class OrdersResponseDTO {
    private int total;
    private List<OrderDTO> active;
    private List<OrderDTO> completed;
    private List<OrderDTO> cancelled;

    // Constructor
    public OrdersResponseDTO(List<OrderDTO> active, List<OrderDTO> completed, List<OrderDTO> cancelled) {
        this.total = active.size() + completed.size() + cancelled.size();
        this.active = active;
        this.completed = completed;
        this.cancelled = cancelled;
    }

}
