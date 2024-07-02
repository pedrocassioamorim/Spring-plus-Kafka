package net.test.springpluskafka.domain.dtos;

import lombok.Data;
import net.test.springpluskafka.domain.entities.OrderEvent;

import java.io.Serializable;

/**
 * DTO for {@link OrderEvent}
 */
@Data
public class OrderEventDto implements Serializable {
    Long id;
    String message;
    String status;
    OrderDto order;

    public OrderEventDto (OrderEvent orderEvent){
        this.id = orderEvent.getId();
        this.message = orderEvent.getMessage();
        this.status = orderEvent.getStatus();
        this.order = new OrderDto(orderEvent.getOrder());
    }

    public OrderEventDto() {}
}