package com.demo.example.repository;

import com.demo.example.model.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Page<Customer> findByPhoneDetailsCountryNameContainsIgnoreCase(String name, Pageable pageable);

    Page<Customer> findByPhoneContainsIgnoreCase(String phone, Pageable pageable);


}