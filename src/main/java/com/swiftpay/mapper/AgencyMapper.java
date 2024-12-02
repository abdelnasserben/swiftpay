package com.swiftpay.mapper;

import com.swiftpay.dto.AgencyDto;
import com.swiftpay.model.Agency;


public class AgencyMapper {

    public static AgencyDto toDto(Agency agency) {

        if (agency == null) {
            return null;
        }

        AgencyDto dto = new AgencyDto();
        dto.setId(agency.getId());
        dto.setName(agency.getName());
        dto.setAddress(agency.getAddress());
        dto.setOffice(OfficeMapper.toDto(agency.getOffice()));

        return dto;
    }

    public static AgencyDto toDtoWithoutOffice(Agency agency) {
        if (agency == null) {
            return null;
        }

        AgencyDto dto = new AgencyDto();
        dto.setId(agency.getId());
        dto.setName(agency.getName());
        dto.setAddress(agency.getAddress());

        // Map Office entity to OfficeDto as null to avoid circular dependency
        dto.setOffice(null);

        return dto;
    }

    public static AgencyDto toDtoWithUsers(Agency agency) {

        AgencyDto dto = toDto(agency);

        dto.setUsers(agency.getUsers().stream()
                .map(UserMapper::toDtoWithoutAgency)
                .toList());

        return dto;
    }
}


