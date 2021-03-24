package com.demo.example.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "phone")
    private String phone;

    //    @Transient
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "phone_details", referencedColumnName = "id")
    private PhoneCodeDetails phoneDetails;

    @Transient
    private Boolean validPhoneNumber;

    public Customer(String name, String phone) {
        this.name = name;
        this.phone = phone;
    }

    public Customer(String name, String phone, PhoneCodeDetails phoneDetails) {
        this.name = name;
        this.phone = phone;
        this.phoneDetails = phoneDetails;
        validatePhoneNumber();
    }

    @PostLoad
    private void validatePhoneNumber() {
        try {
            validPhoneNumber = phone.matches(phoneDetails.getPhoneRegex());
        } catch (RuntimeException exception) {
            validPhoneNumber = false;
        }
    }
}
