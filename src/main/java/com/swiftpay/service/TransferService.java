package com.swiftpay.service;

import com.swiftpay.appUtils.ExchangeUtils;
import com.swiftpay.appUtils.Helper;
import com.swiftpay.dto.CustomerDto;
import com.swiftpay.dto.SendDto;
import com.swiftpay.dto.TransferDto;
import com.swiftpay.enums.Status;
import com.swiftpay.exception.ResourceNotFoundException;
import com.swiftpay.mapper.CustomerMapper;
import com.swiftpay.mapper.TransferMapper;
import com.swiftpay.model.Agency;
import com.swiftpay.model.Customer;
import com.swiftpay.model.Transfer;
import com.swiftpay.model.User;
import com.swiftpay.repository.TransferRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class TransferService {

    private final TransferRepository transferRepository;
    private final CustomerService customerService;
    private final UserService userService;

    public TransferService(TransferRepository transferRepository, CustomerService customerService, UserService userService) {
        this.transferRepository = transferRepository;
        this.customerService = customerService;
        this.userService = userService;
    }

    public TransferDto getByMTCN(String transferMTCN) {
        return TransferMapper.toDto(findByMTCN(transferMTCN));
    }

    private Transfer findByMTCN(String transferMTCN) {
        return transferRepository.findByTransferMTCN(transferMTCN)
                .orElseThrow(() -> new ResourceNotFoundException("Transfer not found"));
    }

    public List<TransferDto> findAll(LocalDate startDate, LocalDate endDate) {

        if (startDate != null && endDate != null) {
            return mapTransfersToDto(transferRepository.findAllByCreatedAtBetween(
                    startDate.atStartOfDay(), endDate.atTime(LocalTime.MAX)));
        }

        return mapTransfersToDto(transferRepository.findAll());
    }

    public List<TransferDto> findUserTransfers(LocalDate startDate, LocalDate endDate) {
        User user = userService.getAuthenticated();
        if (startDate != null && endDate != null) {
            return mapTransfersToDto(transferRepository.findAllByCreatedAtBetweenAndCreatedBy(
                    startDate.atStartOfDay(),
                    endDate.atTime(LocalTime.MAX),
                    user));
        }
        return mapTransfersToDto(transferRepository.findAllByCreatedBy(user));
    }

    public List<TransferDto> findAgencyTransfers(LocalDate startDate, LocalDate endDate) {
        User user = userService.getAuthenticated();
        Agency agency = user.getAgency();
        if (startDate != null && endDate != null) {
            return mapTransfersToDto(transferRepository.findAllByCreatedAtBetweenAndCreatedBy_Agency(
                    startDate.atStartOfDay(),
                    endDate.atTime(LocalTime.MAX),
                    agency));
        }
        return mapTransfersToDto(transferRepository.findAllByCreatedBy_Agency(agency));
    }

    public List<TransferDto> filterTransfers(String status, LocalDate startDate, LocalDate endDate) {

        LocalDateTime startDateTime = startDate != null ? startDate.atStartOfDay() : LocalDateTime.now();
        LocalDateTime endDateTime = endDate != null ? endDate.atTime(LocalTime.MAX) : LocalDateTime.now();

        if (status == null) {
            return mapTransfersToDto(transferRepository.findAllByCreatedAtBetween(startDateTime, endDateTime));
        }

        return mapTransfersToDto(transferRepository.findByStatusIgnoreCaseAndCreatedAtBetween(status, startDateTime, endDateTime));
    }

    private List<TransferDto> mapTransfersToDto(List<Transfer> transfers) {
        return transfers.stream()
                .map(TransferMapper::toDto)
                .toList();
    }


    public static boolean isPayable(TransferDto transfer) {
        return transfer.getStatus().equals(Status.APPROVED.name());
    }

    public String sendMoney(SendDto sendDto) {
        Transfer transfer = buildTransfer(sendDto);
        return transferRepository.save(transfer).getTransferMTCN();
    }

    private Transfer buildTransfer(SendDto sendDto) {

        Transfer transfer = new Transfer();
        transfer.setTransferMTCN(Helper.generate10Digits());

        //TODO: Set transfer sender: if customer exists (by identity type and number), update fields; otherwise, save as new customer.
        Optional<Customer> optionalCustomer = customerService.findByIDTypeAndNumber(sendDto.getIdentityType(), sendDto.getIdentityNumber());
        Customer sender = optionalCustomer
                .map(existingCustomer -> {
                    CustomerDto existingCustomerDto = CustomerMapper.toDto(existingCustomer);
                    CustomerService.updateDifferentFields(existingCustomerDto, sendDto);
                    return customerService.save(existingCustomerDto);
                })
                .orElseGet(() -> customerService.save(sendDto));

        transfer.setSender(sender);

        //TODO: Create and save receiver with provided details.
        CustomerDto receiverDto = new CustomerDto();
        receiverDto.setFirstName(sendDto.getReceiverFirstName());
        receiverDto.setLastName(sendDto.getReceiverLastName());
        receiverDto.setPhoneNumber(sendDto.getReceiverPhone());
        receiverDto.setCity("");
        receiverDto.setAddress("");

        Customer receiver = customerService.save(receiverDto);
        transfer.setReceiver(receiver);

        //TODO: get receiver country

        String[] extractedCountryAndCurrencyArray = Helper.extractCountryAndCurrencyArray(sendDto.getReceiverCountry());
        String receiverCountryName = extractedCountryAndCurrencyArray[0];
        String currencyToPay = extractedCountryAndCurrencyArray[1];

        //TODO: get exchange rates
        double amountToSent = sendDto.getAmountSent();
        double rate = ExchangeUtils.getCurrencyRateFor(currencyToPay);
        double amountToPay = amountToSent * rate;
        double fees = amountToSent * ExchangeUtils.getInterestRate();
        double totalAmount = amountToSent * fees;

        transfer.setReceivingCountry(receiverCountryName);
        transfer.setCurrencyToPay(currencyToPay);
        transfer.setRate(rate);

        transfer.setAmountSent(amountToSent);
        transfer.setAmountToPay(amountToPay);
        transfer.setFees(fees);
        transfer.setTotalAmount(totalAmount);
        transfer.setIssuingCountry(ExchangeUtils.getCountryOrigin());
        transfer.setCurrencySent(ExchangeUtils.getBaseCurrency());

        transfer.setStatus(Status.INITIATED.name());
        transfer.setCreatedBy(userService.getAuthenticated());
        return transfer;
    }

    public void pay(String transferMTCN, CustomerDto receiverDto) {
        Transfer transfer = transferRepository.findByTransferMTCN(transferMTCN)
                .orElseThrow(() -> new ResourceNotFoundException("Transfer not found"));

        //TODO: Update receiver information if necessary
        CustomerDto currentReceiver = CustomerMapper.toDto(transfer.getReceiver());
        CustomerService.updateDifferentFields(currentReceiver, receiverDto);
        customerService.save(currentReceiver);

        transfer.setStatus(Status.PAID.name());
        transferRepository.save(transfer);
    }

    public void refund(String transferMTCN) {
        updateTransferStatus(transferMTCN, Status.REFUNDED.name());
    }

    public void lock(String transferMTCN) {
        updateTransferStatus(transferMTCN, Status.LOCKED.name());
    }

    public void approve(String transferMTCN) {
        updateTransferStatus(transferMTCN, Status.APPROVED.name());
    }

    private void updateTransferStatus(String transferMTCN, String status) {
        Transfer transfer = findByMTCN(transferMTCN);

        if(!transfer.getStatus().equals(Status.PAID.name()) && !transfer.getStatus().equals(status)) {
            transfer.setStatus(status);
            transferRepository.save(transfer);
        }
    }
}
