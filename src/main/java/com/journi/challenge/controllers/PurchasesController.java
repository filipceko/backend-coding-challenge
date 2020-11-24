package com.journi.challenge.controllers;

import com.journi.challenge.CurrencyConverter;
import com.journi.challenge.models.Purchase;
import com.journi.challenge.models.PurchaseRequest;
import com.journi.challenge.models.PurchaseStats;
import com.journi.challenge.repositories.PurchasesRepository;
import com.journi.challenge.repositories.StatisticsRepository;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
public class PurchasesController {

    @Inject
    private PurchasesRepository purchasesRepository;
    @Inject
    private StatisticsRepository statisticsRepository;
    @Inject
    private CurrencyConverter currencyConverter;

    @GetMapping("/purchases/statistics")
    public PurchaseStats getStats() {
        return statisticsRepository.getStatistics();
    }

    @PostMapping("/purchases")
    public Purchase save(@RequestBody PurchaseRequest purchaseRequest) {
        Purchase newPurchase = new Purchase(
                purchaseRequest.getInvoiceNumber(),
                LocalDateTime.parse(purchaseRequest.getDateTime(), DateTimeFormatter.ISO_DATE_TIME),
                purchaseRequest.getProductIds(),
                purchaseRequest.getCustomerName(),
                //Always store as EURO
                currencyConverter.convertCurrencyToEur(purchaseRequest.getCurrencyCode(), purchaseRequest.getAmount())
        );
        purchasesRepository.save(newPurchase);
        statisticsRepository.addPurchase(newPurchase);
        return newPurchase;
    }
}
