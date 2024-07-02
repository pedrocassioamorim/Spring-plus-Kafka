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

}