package com.demo.example.controller;

import com.demo.example.exceptions.CustomerNotFoundException;
import com.demo.example.model.Customer;
import com.demo.example.model.PhoneCodeDetails;
import com.demo.example.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(SpringExtension.class)
@SpringBootTest
public class CustomerControllerTest {
    @InjectMocks
    CustomerController customerController;

    @Mock
    CustomerRepository customerRepository;

    @BeforeEach
    private void initialize() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
    }

    @Test
    public void testGetAll() {
        List<Customer> customerList = new ArrayList<>();
        customerList.add(new Customer("name1", "(212) 654642448"));
        customerList.add(new Customer("name2", "(258) 84330678235"));
        customerList.add(new Customer("name3", "(212) 6617344445"));

        Page<Customer> pagedCustomers = new PageImpl(customerList);

        when(customerRepository.findAll(any(Pageable.class))).thenReturn(pagedCustomers);
        ResponseEntity<Map<String, Object>> responseEntity = customerController.getAll(null, null, null, 0, 20, "id", "desc");
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
        assertThat(responseEntity.getBody().size()).isEqualTo(4);

    }

    @Test
    public void testNotFoundCustomer() {
        List<Customer> customerList = new ArrayList<>();
        customerList.add(new Customer("name1", "(212) 654642448"));

        Page<Customer> pagedCustomers = new PageImpl(customerList);

        when(customerRepository.findAll(any(Pageable.class))).thenReturn(pagedCustomers);

        assertThrows(CustomerNotFoundException.class, () -> {
            customerController.getCustomerDetails(100l);
        });
    }

    @Test
    public void testValidPhoneNumber() {

        PhoneCodeDetails phoneDetails = new PhoneCodeDetails();
        phoneDetails.setId(1l);
        phoneDetails.setCountryCode("212");
        phoneDetails.setPhoneRegex("\\(212\\)\\s?[5-9]\\d{8}$");


        Customer customer = new Customer("name1", "(212) 654642448", phoneDetails);

        Page<Customer> pagedCustomers = new PageImpl(Collections.singletonList(customer));

        when(customerRepository.findAll(any(Pageable.class))).thenReturn(pagedCustomers);
        ResponseEntity<Map<String, Object>> responseEntity = customerController.getAll(null, null, null, 0, 20, "id", "desc");
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
        List<Customer> retrievedCustomer = (List<Customer>) responseEntity.getBody().get("customers");
        assertThat(retrievedCustomer.get(0).getValidPhoneNumber()).isEqualTo(true);

    }

    @Test
    public void testInValidPhoneNumber() {

        PhoneCodeDetails phoneDetails = new PhoneCodeDetails();
        phoneDetails.setId(1l);
        phoneDetails.setCountryCode("12");
        phoneDetails.setPhoneRegex("\\(12\\)\\s?[5-9]\\d{8}$");


        Customer customer = new Customer("name1", "(212) 123456789", phoneDetails);

        Page<Customer> pagedCustomers = new PageImpl(Collections.singletonList(customer));

        when(customerRepository.findAll(any(Pageable.class))).thenReturn(pagedCustomers);
        ResponseEntity<Map<String, Object>> responseEntity = customerController.getAll(null, null, null, 0, 20, "id", "desc");
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
        List<Customer> retrievedCustomer = (List<Customer>) responseEntity.getBody().get("customers");
        assertThat(retrievedCustomer.get(0).getValidPhoneNumber()).isEqualTo(false);

    }


    @Test
    public void testAddNewCustomer() {

        Customer newCustomer = new Customer("customer name", "(237) 695539786");
        when(customerRepository.save(any(Customer.class))).thenReturn(newCustomer);
        ResponseEntity<Object> responseEntity = customerController.addNewCustomer(newCustomer);
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(201);
        assertThat(((Customer) responseEntity.getBody()).getName()).isEqualTo(newCustomer.getName());
    }

    @Test
    public void testGetCustomerDetails() {
        Customer aCustomer = new Customer("customer name", "(237) 691816558");
        aCustomer.setId(1l);

        when(customerRepository.findById(any(Long.class))).thenReturn(java.util.Optional.of(aCustomer));
        Customer returnedCustomer = customerController.getCustomerDetails(1l);
        assertThat((returnedCustomer.getId()).equals(1l));
    }

    @Test
    public void testUpdateCustomer() {
        Customer aCustomer = new Customer("customer name", "(237) 691816111");
        aCustomer.setId(1l);

        Customer newCustomer = new Customer("new customer name", "(237) 691816222");

        when(customerRepository.findById(any(Long.class))).thenReturn(java.util.Optional.of(aCustomer));
        when(customerRepository.save(any(Customer.class))).thenReturn(newCustomer);
        Customer updatedCustomer = customerController.updateCustomer(newCustomer, 1l);

        assertThat((updatedCustomer.getName()).equals(newCustomer.getName()));
        assertThat((updatedCustomer.getPhone()).equals(newCustomer.getPhone()));
    }

    @Test
    public void testDeleteCustomer() {
        ResponseEntity<Object> responseEntity = customerController.deleteCustomer(1l);
        verify(customerRepository).deleteById(any());
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
    }

}
