package net.test.springpluskafka.controllers;

import net.test.springpluskafka.domain.dtos.OrderDto;
import net.test.springpluskafka.services.OrderService;
import net.test.springpluskafka.services.exceptions.DatabaseViolationException;
import net.test.springpluskafka.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/listed")
    public ResponseEntity<List<OrderDto>> findAllListed(){
        List<OrderDto> list = orderService.findAllListed();
        return ResponseEntity.ok().body(list);
    }

    @GetMapping("/paged")
    public ResponseEntity<Page<OrderDto>> findAllPaged(Pageable page){
        Page<OrderDto> pageRequested = orderService.findAllPaged((PageRequest) page);
        return ResponseEntity.ok().body(pageRequested);
    }

    @GetMapping("/{Id}")
    public ResponseEntity<?> findById(@PathVariable Long Id){
        try{
            OrderDto orderDto = orderService.findById(Id);
            return ResponseEntity.ok().body(orderDto);
        } catch (ResourceNotFoundException e){
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<OrderDto> insert(@RequestBody OrderDto orderDto){
        orderDto = orderService.insert(orderDto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{Id}")
                .buildAndExpand(orderDto.getId()).toUri();
        return ResponseEntity.created(uri).body(orderDto);
    }

    @PutMapping("/{Id}")
    public ResponseEntity<?> update(@PathVariable Long Id, @RequestBody OrderDto orderDto){
        try{
            orderDto = orderService.update(Id, orderDto);
            return ResponseEntity.ok().body(orderDto);
        } catch (ResourceNotFoundException e){
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{Id}")
    public ResponseEntity<?> delete(@PathVariable Long Id){
        try{
            orderService.delete(Id);
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException e){
            return ResponseEntity.notFound().build();
        } catch (DatabaseViolationException f){
            return ResponseEntity.badRequest().body(f.getMessage());
        }
    }
}
