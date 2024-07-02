package net.test.springpluskafka.dtos;

import lombok.Value;
import net.test.springpluskafka.entities.OrderEvent;

import java.io.Serializable;

/**
 * DTO for {@link OrderEvent}
 */
@Value
public class OrderEventDto implements Serializable {
    Long id;
    String message;
    String status;
    OrderDto order;
}