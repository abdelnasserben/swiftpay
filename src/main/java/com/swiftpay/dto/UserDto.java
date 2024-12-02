package com.swiftpay.dto;

public class UserDto {

    private Long id;
    private String username;
    private String password;
    private AgencyDto agency;
    private String rolesAsString;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public AgencyDto getAgency() {
        return agency;
    }

    public void setAgency(AgencyDto agency) {
        this.agency = agency;
    }

    public String getRolesAsString() {
        return rolesAsString;
    }

    public void setRolesAsString(String rolesAsString) {
        this.rolesAsString = rolesAsString;
    }
}
