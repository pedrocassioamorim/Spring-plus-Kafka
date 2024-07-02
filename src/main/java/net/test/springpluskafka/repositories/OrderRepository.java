package net.test.springpluskafka.repositories;

import net.test.springpluskafka.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
