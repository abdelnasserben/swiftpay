package com.swiftpay.mapper;

import com.swiftpay.dto.CurrencyDto;
import com.swiftpay.dto.OfficeDto;
import com.swiftpay.model.Currency;
import com.swiftpay.model.Office;

import java.util.stream.Collectors;

public class OfficeMapper {

    public static OfficeDto toDto(Office office) {
        if (office == null) {
            return null;
        }

        OfficeDto dto = getOfficeDtoWithoutAgencies(office);

        // Convert list of Agency to list of AgencyDto using AgencyMapper without map Office entity to OfficeDto to avoid circular dependency
        dto.setAgencies(office.getAgencies().stream()
                .map(AgencyMapper::toDtoWithoutOffice)
                .collect(Collectors.toList()));

        return dto;
    }

    public static OfficeDto toDtoWithoutAgencies(Office office) {
        if (office == null) {
            return null;
        }

        OfficeDto dto = getOfficeDtoWithoutAgencies(office);
        dto.setAgencies(null);

        return dto;
    }

    private static OfficeDto getOfficeDtoWithoutAgencies(Office office) {
        OfficeDto dto = new OfficeDto();
        dto.setId(office.getId());
        dto.setCodeISO(office.getCodeISO());
        dto.setName(office.getName());
        dto.setCurrency(office.getCurrency());
        dto.setInterestRate(office.getInterestRate());

        dto.setCurrencies(office.getCurrencies().stream()
                .map(CurrencyMapper::toDto)
                .collect(Collectors.toList()));
        return dto;
    }

    public static Office toEntity(OfficeDto dto) {
        if (dto == null) {
            return null;
        }

        Office office = new Office();
        office.setId(dto.getId());
        office.setCodeISO(dto.getCodeISO());
        office.setName(dto.getName());
        office.setCurrency(dto.getCurrency());
        office.setInterestRate(dto.getInterestRate());

        //we ignore currencies and agencies

        return office;
    }


    /**
     * Inner class for mapping Currency and CurrencyDto.
     */

    private static class CurrencyMapper {

        public static CurrencyDto toDto(Currency currency) {
            if (currency == null) {
                return null;
            }

            CurrencyDto dto = new CurrencyDto();
            dto.setId(currency.getId());
            dto.setCode(currency.getCode());
            // Map Office entity to OfficeDto as null to avoid circular dependency
            dto.setOffice(null);
            return dto;
        }
    }
}
