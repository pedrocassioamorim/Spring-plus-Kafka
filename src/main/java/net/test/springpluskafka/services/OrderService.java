package net.test.springpluskafka.services;

import jakarta.transaction.Transactional;
import net.test.springpluskafka.domain.dtos.OrderDto;
import net.test.springpluskafka.domain.entities.Order;
import net.test.springpluskafka.repositories.OrderRepository;
import net.test.springpluskafka.services.exceptions.DatabaseViolationException;
import net.test.springpluskafka.services.exceptions.ResourceNotFoundException;
import org.apache.kafka.common.network.Mode;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PagedModel;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;


    @Transactional
    public List<OrderDto> findAllListed() {
        List<Order> orders = orderRepository.findAll();
        return orders.stream().map(OrderDto::new).toList();
    }

    @Transactional
    public PagedModel<OrderDto> findAllPaged(PageRequest pageRequest){
        Logger LOGGER = LoggerFactory.getLogger(OrderService.class);
        Page<Order> page = orderRepository.findAll(pageRequest);
        if (page.isEmpty()){
            LOGGER.info("Empty page returned. Check data and filtering variables.");
        }
        List<OrderDto> orderDtos = page.getContent().stream().map(order -> new OrderDto()).toList();
        Page<OrderDto> pageDTO = new PageImpl<>(orderDtos);
        return new PagedModel<>(pageDTO);
    }

    @Transactional
    public OrderDto findById(Long Id){
        ModelMapper modelMapper = new ModelMapper();
        Optional<Order> order = orderRepository.findById(Id);
        if (order.isEmpty()) {
            throw new ResourceNotFoundException("Object not found (ID): " + Id);
        }
        return modelMapper.map(order, OrderDto.class);
    }

    @Transactional
    public OrderDto insert(OrderDto orderDto){
        ModelMapper modelMapper = new ModelMapper();
        Order order = modelMapper.map(orderDto, Order.class);
        return modelMapper.map(orderRepository.save(order), OrderDto.class);
    }

    @Transactional
    public OrderDto update(Long Id, OrderDto orderDto){
        ModelMapper modelMapper = new ModelMapper();
        Optional<Order> order = orderRepository.findById(Id);
        if (order.isEmpty()){
            throw new ResourceNotFoundException("Object not found (ID): " + Id);
        }
        Order newOrder = modelMapper.map(orderDto, Order.class);
        newOrder.setId(Id);
        return modelMapper.map(orderRepository.save(newOrder), OrderDto.class);
    }

    @Transactional
    public void delete(Long Id){
        ModelMapper modelMapper = new ModelMapper();
        Optional<Order> order = orderRepository.findById(Id);
        if(order.isEmpty()){
            throw new ResourceNotFoundException("Object not found (ID): " + Id);
        }
        try{
            Order orderToDelete = modelMapper.map(order, Order.class);
            orderRepository.delete(orderToDelete);
        } catch (DataIntegrityViolationException e){
            throw new DatabaseViolationException("Data Integrity Violation! Object not deleted. Stacktrace: " + Arrays.toString(e.getStackTrace()));
        }
    }

}
