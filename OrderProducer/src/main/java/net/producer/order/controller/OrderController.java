package net.producer.order.controller;

import net.producer.order.kafka.OrderProducer;
import net.test.springpluskafka.domain.dtos.OrderDto;
import net.test.springpluskafka.domain.dtos.OrderEventDto;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController @RequestMapping("/api/v1")
public class OrderController {

    private OrderProducer orderProducer;

    public OrderController(OrderProducer orderProducer) {
        this.orderProducer = orderProducer;
    }

    @PostMapping("/orders")
    public String placeOrder(@RequestBody OrderDto orderDto){
        OrderEventDto orderEventDto = new OrderEventDto();
        orderEventDto.setStatus("PENDING");
        orderEventDto.setMessage("Order status is in pending state");
        orderEventDto.setOrder(orderDto);

        orderProducer.sendMessage(orderEventDto);
        return "Order placed seccessfully";
    }
}
