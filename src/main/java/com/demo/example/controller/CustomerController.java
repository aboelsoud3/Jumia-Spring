package com.demo.example.controller;

import com.demo.example.exceptions.CustomerNotFoundException;
import com.demo.example.model.Customer;
import com.demo.example.repository.CustomerRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/customer")
public class CustomerController {
    private final CustomerRepository repository;

    public CustomerController(CustomerRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/")
    ResponseEntity<Map<String, Object>> getAll(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String isValid,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection) {
        try {
            List<Customer> customers;
            Pageable paging = PageRequest.of(page, size);

            Page<Customer> customerPage;
            if (name != null) {
                customerPage = repository.findByPhoneDetailsCountryNameContainsIgnoreCase(name, paging);
            } else if (phone != null) {
                customerPage = repository.findByPhoneContainsIgnoreCase(phone, paging);
            } else {
                Pageable pagingSort = getPagingSort(sortBy, sortDirection, page, size);
                customerPage = repository.findAll(pagingSort);
            }


            customers = customerPage.getContent();
            if (isValid != null) {
                customers = customers.stream()
                        .filter(customer -> customer.getValidPhoneNumber() == isValid.equals("true")).collect(Collectors.toList());
            }

            if (customers.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            Map<String, Object> response = new HashMap<>();
            response.put("customers", customers);
            response.put("currentPage", customerPage.getNumber());
            response.put("totalItems", customerPage.getTotalElements());
            response.put("totalPages", customerPage.getTotalPages());

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping("/")
    ResponseEntity<Object> addNewCustomer(@RequestBody Customer newCustomer) {
        try {
            newCustomer = repository.save(newCustomer);
            return new ResponseEntity<Object>(newCustomer, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping("/{id}")
    Customer getCustomerDetails(@PathVariable Long id) {

        return repository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(id));
    }

    @PutMapping("/{id}")
    Customer updateCustomer(@RequestBody Customer newCustomer, @PathVariable Long id) {

        return repository.findById(id)
                .map(Customer -> {
                    Customer.setName(newCustomer.getName());
                    Customer.setName(newCustomer.getName());
                    Customer.setName(newCustomer.getPhone());
                    return repository.save(Customer);
                })
                .orElseGet(() -> {
                    newCustomer.setId(id);
                    return repository.save(newCustomer);
                });
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Object> deleteCustomer(@PathVariable Long id) {
        try {
            repository.deleteById(id);
            return new ResponseEntity<>(null, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private Pageable getPagingSort(String sortBy, String direction, int page, int size) {
        List<Sort.Order> orders = new ArrayList<Sort.Order>();
        orders.add(new Sort.Order(getSortDirection(direction), sortBy));
        Pageable pagingSort = PageRequest.of(page, size, Sort.by(orders));

        return pagingSort;
    }

    private Sort.Direction getSortDirection(String direction) {
        if (direction.equals("desc")) {
            return Sort.Direction.DESC;
        } else {
            return Sort.Direction.ASC;
        }
    }
}
