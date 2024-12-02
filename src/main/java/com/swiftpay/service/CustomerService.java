package com.swiftpay.service;

import com.swiftpay.dto.CustomerDto;
import com.swiftpay.mapper.CustomerMapper;
import com.swiftpay.model.Customer;
import com.swiftpay.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.Optional;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Customer save(CustomerDto customerDto) {
        return customerRepository.save(CustomerMapper.toEntity(customerDto));
    }

    public Optional<Customer> findByIDTypeAndNumber(String identityType, String identityNumber) {
        return customerRepository.findByIdentityTypeAndIdentityNumber(identityType, identityNumber);
    }

    public CustomerDto getByIDTypeAndNumber(String identityType, String identityNumber) {
        return CustomerMapper.toDto(findByIDTypeAndNumber(identityType, identityNumber).orElse(null));
    }

    public static void updateDifferentFields(CustomerDto target, CustomerDto source) {
        for (Field field : CustomerDto.class.getDeclaredFields()) {
            field.setAccessible(true);

            try {
                Object newValue = field.get(source);
                Object existingValue = field.get(target);

                if (newValue != null && !newValue.toString().isEmpty() && !newValue.equals(existingValue)) {
                    field.set(target, newValue);
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Failed to access field during update", e);
            }
        }
    }
}
