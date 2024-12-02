package com.swiftpay.dto;

import java.util.ArrayList;
import java.util.List;

public class AgencyDto {

    private Long id;

    private String name;

    private String address;

    private OfficeDto office;

    private List<UserDto> users = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public OfficeDto getOffice() {
        return office;
    }

    public void setOffice(OfficeDto office) {
        this.office = office;
    }

    public List<UserDto> getUsers() {
        return users;
    }

    public void setUsers(List<UserDto> users) {
        this.users = users;
    }
}

