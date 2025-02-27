package net.test.springpluskafka.services;

import jakarta.persistence.EntityNotFoundException;
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
    public Page<OrderDto> findAllPaged(PageRequest pageRequest){
        ModelMapper modelMapper = new ModelMapper();
        Logger LOGGER = LoggerFactory.getLogger(OrderService.class);
        List<Order> list = orderRepository.findAll();
        List<OrderDto> listDTO = list.stream().map(OrderDto::new).toList();
        return new PageImpl<>(listDTO);
    }

    @Transactional
    public OrderDto findById(Long Id) {
        try{
            ModelMapper modelMapper = new ModelMapper();
            Order order = orderRepository.getReferenceById(Id);
            if (order.getName().isEmpty()) {
                throw new ResourceNotFoundException("Object not found (ID): " + Id);
            }
            return modelMapper.map(order, OrderDto.class);
        } catch (EntityNotFoundException f){
            throw new ResourceNotFoundException("Object not found (ID): " + Id);
        }
    }

    @Transactional
    public OrderDto insert(OrderDto orderDto){
        ModelMapper modelMapper = new ModelMapper();
        Order order = modelMapper.map(orderDto, Order.class);
        return modelMapper.map(orderRepository.save(order), OrderDto.class);
    }

    @Transactional
    public OrderDto update(Long Id, OrderDto orderDto){
        try{
            ModelMapper modelMapper = new ModelMapper();
            Order order = orderRepository.getReferenceById(Id);
            if (order.getName().isEmpty()){
                throw new ResourceNotFoundException("Object not found (ID): " + Id);
            }
            order = modelMapper.map(orderDto, Order.class);
            order.setId(Id);
            return modelMapper.map(orderRepository.save(order), OrderDto.class);
        }catch (EntityNotFoundException f){
            throw new ResourceNotFoundException("Object not found (ID): " + Id);
        }


    }

    @Transactional
    public void delete(Long Id){
        try{
            ModelMapper modelMapper = new ModelMapper();
            Order order = orderRepository.getReferenceById(Id);
            if(order.getName().isEmpty()){
                throw new ResourceNotFoundException("Object not found (ID): " + Id);
            }
            orderRepository.delete(order);
        } catch (DataIntegrityViolationException e){
            throw new DatabaseViolationException("Data Integrity Violation! Object not deleted. Stacktrace: " + Arrays.toString(e.getStackTrace()));
        } catch (EntityNotFoundException f){
            throw new ResourceNotFoundException("Object not found (ID): " + Id);
        }
    }

}
