package net.test.springpluskafka.dtos;

import lombok.Value;
import net.test.springpluskafka.entities.Order;

import java.io.Serializable;

/**
 * DTO for {@link Order}
 */
@Value
public class OrderDto implements Serializable {
    Long id;
    String name;
    Integer quantity;
    Double price;
}