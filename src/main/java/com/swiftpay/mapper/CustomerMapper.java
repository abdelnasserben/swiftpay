package com.swiftpay.mapper;

import com.swiftpay.dto.CustomerDto;
import com.swiftpay.model.Customer;

public class CustomerMapper {

    public static CustomerDto toDto(Customer customer) {
        if (customer == null) {
            return null;
        }

        CustomerDto dto = new CustomerDto();
        dto.setId(customer.getId());
        dto.setFirstName(customer.getFirstName());
        dto.setLastName(customer.getLastName());
        dto.setDateOfBirth(customer.getDateOfBirth());
        dto.setIdentityType(customer.getIdentityType());
        dto.setIdentityNumber(customer.getIdentityNumber());
        dto.setIdentityExpirationDate(customer.getIdentityExpirationDate());
        dto.setCountryIssuingIdentity(customer.getCountryIssuingIdentity());
        dto.setNationality(customer.getNationality());
        dto.setCity(customer.getCity());
        dto.setAddress(customer.getAddress());
        dto.setPostalCode(customer.getPostalCode());
        dto.setPhoneNumber(customer.getPhoneNumber());
        dto.setCreatedAt(customer.getCreatedAt());
        dto.setUpdatedAt(customer.getUpdatedAt());

        return dto;
    }

    public static Customer toEntity(CustomerDto dto) {
        if (dto == null) {
            return null;
        }

        Customer customer = new Customer();
        customer.setId(dto.getId());
        customer.setFirstName(dto.getFirstName());
        customer.setLastName(dto.getLastName());
        customer.setDateOfBirth(dto.getDateOfBirth());
        customer.setIdentityType(dto.getIdentityType());
        customer.setIdentityNumber(dto.getIdentityNumber());
        customer.setIdentityExpirationDate(dto.getIdentityExpirationDate());
        customer.setCountryIssuingIdentity(dto.getCountryIssuingIdentity());
        customer.setNationality(dto.getNationality());
        customer.setCity(dto.getCity());
        customer.setAddress(dto.getAddress());
        customer.setPostalCode(dto.getPostalCode());
        customer.setPhoneNumber(dto.getPhoneNumber());
        customer.setCreatedAt(dto.getCreatedAt());
        customer.setUpdatedAt(dto.getUpdatedAt());

        return customer;
    }
}

