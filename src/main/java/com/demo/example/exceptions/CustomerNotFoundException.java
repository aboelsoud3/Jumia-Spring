package com.demo.example.exceptions;

public class CustomerNotFoundException extends RuntimeException {

    public CustomerNotFoundException(Long id) {
        super("Could not find Customer with Id = " + id);
    }
}