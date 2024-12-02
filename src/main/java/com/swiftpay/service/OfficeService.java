package com.swiftpay.service;

import com.swiftpay.dto.AgencyDto;
import com.swiftpay.dto.OfficeDto;
import com.swiftpay.enums.Country;
import com.swiftpay.exception.IllegalOperationException;
import com.swiftpay.exception.ResourceNotFoundException;
import com.swiftpay.mapper.AgencyMapper;
import com.swiftpay.mapper.OfficeMapper;
import com.swiftpay.model.Agency;
import com.swiftpay.model.Currency;
import com.swiftpay.model.Office;
import com.swiftpay.repository.AgencyRepository;
import com.swiftpay.repository.CurrencyRepository;
import com.swiftpay.repository.OfficeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OfficeService {

    private final OfficeRepository officeRepo;
    private final AgencyRepository agencyRepo;
    private final CurrencyRepository currencyRepo;

    public OfficeService(OfficeRepository officeRepo, AgencyRepository agencyRepo, CurrencyRepository currencyRepo) {
        this.officeRepo = officeRepo;
        this.agencyRepo = agencyRepo;
        this.currencyRepo = currencyRepo;
    }

    public List<OfficeDto> getAllOffices() {
        return officeRepo.findAll().stream()
                .map(OfficeMapper::toDtoWithoutAgencies)
                .toList();
    }

    public OfficeDto getOfficeByCodeIso(String isoCode) {
        Office office = officeRepo.findByCodeISO(isoCode)
                .orElseThrow(() -> new ResourceNotFoundException("Office with ISO code " + isoCode + " not found"));
        return OfficeMapper.toDto(office);
    }

    public List<AgencyDto> getAllAgencies() {
        return agencyRepo.findAll().stream()
                .map(AgencyMapper::toDto)
                .toList();
    }

    public AgencyDto getAgencyById(Long id) {
        Agency office = agencyRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Agency not found"));
        return AgencyMapper.toDtoWithUsers(office);
    }

    public void createNewOffice(String isoCode, double interestRate, boolean acceptUsd, boolean acceptEur) {
        checkIfOfficeExists(isoCode);

        Country country = Country.fromCode(isoCode);

        Office office = new Office();
        office.setCodeISO(country.getCodeISO());
        office.setName(country.getName());
        office.setCurrency(country.getCurrency());
        office.setInterestRate(interestRate);

        Office savedOffice = officeRepo.save(office);
        saveOfficeCurrencies(savedOffice, acceptUsd, acceptEur);
    }

    private void checkIfOfficeExists(String isoCode) {
        if (officeRepo.findByCodeISO(isoCode).isPresent()) {
            throw new IllegalOperationException("Office already exists for this country.");
        }
    }

    private void saveOfficeCurrencies(Office office, boolean acceptUsd, boolean acceptEur) {
        createOfficeCurrency(office, office.getCurrency());

        if (acceptUsd && !office.getCurrency().equals("USD")) {
            createOfficeCurrency(office, "USD");
        }
        if (acceptEur && !office.getCurrency().equals("EUR")) {
            createOfficeCurrency(office, "EUR");
        }
    }

    private void createOfficeCurrency(Office office, String currencyCode) {
        Currency currency = new Currency();
        currency.setOffice(office);
        currency.setCode(currencyCode);

        currencyRepo.save(currency);
    }

    public void createNewAgency(String isoCode, String agencyName, String agencyAddress) {
        Office office = officeRepo.findByCodeISO(isoCode)
                .orElseThrow(() -> new IllegalOperationException("Office with ISO code " + isoCode + " not found"));

        Agency agency = new Agency();
        agency.setOffice(office);
        agency.setName(agencyName);
        agency.setAddress(agencyAddress);

        agencyRepo.save(agency);
    }
}