package com.swiftpay;

import com.swiftpay.enums.Country;
import com.swiftpay.model.Currency;
import com.swiftpay.model.*;
import com.swiftpay.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class DataLoader {

    @Bean
    public CommandLineRunner initialize(UserRepository userRepository, RoleRepository roleRepository, OfficeRepository officeRepository, CurrencyRepository currencyRepository, AgencyRepository agencyRepository) {

        List<Country> countryList = new ArrayList<>(List.of(Country.values()));
        countryList.sort(Comparator.comparing(Country::getName));

        return args -> {
            for (Country country: countryList) {

                Office office = new Office();
                office.setCodeISO(country.getCodeISO());
                office.setName(country.getName());
                office.setCurrency(country.getCurrency());

                // Attribute a random interest rate between 1% and 4%
                double interestRate = 1 + Math.random() * 3;
                interestRate = Math.round(interestRate * 100.0) / 100.0;
                office.setInterestRate(interestRate);

                // save office
                officeRepository.save(office);

                // Save national currency as officeCurrency
                createCurrencyLink(currencyRepository, office, country.getCurrency());

                // Link supplementary currencies (USD or EUR)
                if (isUSDCurrencyLinkRequired(country.getCodeISO()) && !country.getCurrency().equals("USD")) {
                    createCurrencyLink(currencyRepository, office, "USD");
                }
                if (isEuroCurrencyLinkRequired(country.getCodeISO()) && !country.getCurrency().equals("EUR")) {
                    createCurrencyLink(currencyRepository, office, "EUR");
                }

                // Create Agency for office
                createAgency(agencyRepository, office);
            }

            createUsers(userRepository, roleRepository, agencyRepository);
        };
    }

    private void createUsers(UserRepository userRepository, RoleRepository roleRepository, AgencyRepository agencyRepository) {

        List<Agency> savedAgencies = agencyRepository.findAll();

        User user1 = new User("john", PasswordEncoderFactories.createDelegatingPasswordEncoder().encode("123"));
        user1.setAgency(savedAgencies.get(0));

        User user2 = new User("josh", PasswordEncoderFactories.createDelegatingPasswordEncoder().encode("123"));
        user2.setAgency(savedAgencies.get(1));

        User savedUser1 = userRepository.save(user1);
        User savedUser2 = userRepository.save(user2);

        roleRepository.save(new Role(savedUser1, "ROLE_EMPLOYEE"));
        roleRepository.save(new Role(savedUser2, "ROLE_ADMIN"));
    }

    private void createAgency(AgencyRepository agencyRepository, Office office) {

        Random random = new Random();
        Agency agency = new Agency();
        agency.setName(UUID.randomUUID().toString().substring(0, 10));
        agency.setAddress(String.format("%d Street, %s",random.nextInt(1, 200), office.getName()));

        agency.setOffice(office);
        agencyRepository.save(agency);
    }

    private void createCurrencyLink(CurrencyRepository currencyRepository, Office office, String currencyCode) {
        Currency currency = new Currency();
        currency.setOffice(office);
        currency.setCode(currencyCode);

        currencyRepository.save(currency);
    }

    private boolean isEuroCurrencyLinkRequired(String countryIsoCode) {
        // List of countries where EUR is commonly used as an additional currency
        Set<String> countriesWithAdditionalCurrencies = new HashSet<>(List.of("CH", "MA", "DZ", "TN", "EG", "TR", "RU", "UA", "CN", "IN", "NG", "ZA", "BR", "MX", "SA"));
        return countriesWithAdditionalCurrencies.contains(countryIsoCode);
    }

    private boolean isUSDCurrencyLinkRequired(String countryIsoCode) {
        // List of countries where USD is commonly used as an additional currency
        Set<String> countriesWithAdditionalCurrencies = new HashSet<>(List.of("PH", "MX", "GT", "HN", "PA", "CR", "CO", "VE", "NG", "GH", "KE", "SA", "AE", "CN", "IN", "PK", "EG", "RU"));
        return countriesWithAdditionalCurrencies.contains(countryIsoCode);
    }


}

