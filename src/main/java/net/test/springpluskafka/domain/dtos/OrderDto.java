package net.test.springpluskafka.domain.dtos;

import lombok.Data;
import net.test.springpluskafka.domain.entities.Order;

import java.io.Serializable;

/**
 * DTO for {@link Order}
 */
@Data
public class OrderDto implements Serializable {
    Long id;
    String name;
    Integer quantity;
    Double price;

    public OrderDto (Order order){
        this.id = order.getId();
        this.name = order.getName();
        this.price = order.getPrice();
        this.quantity = order.getQuantity();
    }

    public OrderDto() {}
}