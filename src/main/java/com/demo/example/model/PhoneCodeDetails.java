package com.demo.example.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "countries_phone_codes")
public class PhoneCodeDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "country")
    private String countryName;

    @Column(name = "code")
    private String countryCode;

    @Column(name = "validation_regx")
    private String phoneRegex;
}
