package net.test.springpluskafka.repositories;

import net.test.springpluskafka.domain.entities.OrderEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderEventRepository extends JpaRepository<OrderEvent, Long> {
}