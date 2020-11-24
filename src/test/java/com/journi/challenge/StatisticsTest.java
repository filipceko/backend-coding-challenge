package com.journi.challenge;

import com.journi.challenge.controllers.PurchasesController;
import com.journi.challenge.models.Purchase;
import com.journi.challenge.models.PurchaseStats;
import com.journi.challenge.repositories.StatisticsRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureMockMvc
public class StatisticsTest {

    @Autowired
    PurchasesController purchasesController;
    @Autowired
    StatisticsRepository purchasesRepository;

    @Test
    public void testPurchaseStatistics() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime firstDate = now.minusDays(20);
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE.withZone(ZoneId.of("UTC"));
        // Inside window purchases
        purchasesRepository.addPurchase(new Purchase("1", firstDate, Collections.emptyList(), "", 10.0));
        purchasesRepository.addPurchase(new Purchase("1", firstDate.plusDays(1), Collections.emptyList(), "", 10.0));
        purchasesRepository.addPurchase(new Purchase("1", firstDate.plusDays(2), Collections.emptyList(), "", 10.0));
        purchasesRepository.addPurchase(new Purchase("1", firstDate.plusDays(3), Collections.emptyList(), "", 10.0));
        purchasesRepository.addPurchase(new Purchase("1", firstDate.plusDays(4), Collections.emptyList(), "", 10.0));
        purchasesRepository.addPurchase(new Purchase("1", firstDate.plusDays(5), Collections.emptyList(), "", 10.0));
        purchasesRepository.addPurchase(new Purchase("1", firstDate.plusDays(6), Collections.emptyList(), "", 10.0));
        purchasesRepository.addPurchase(new Purchase("1", firstDate.plusDays(7), Collections.emptyList(), "", 10.0));
        purchasesRepository.addPurchase(new Purchase("1", firstDate.plusDays(8), Collections.emptyList(), "", 10.0));
        purchasesRepository.addPurchase(new Purchase("1", firstDate.plusDays(9), Collections.emptyList(), "", 10.0));

        PurchaseStats purchaseStats = purchasesController.getStats();
        assertEquals(formatter.format(firstDate), purchaseStats.getFrom());
        assertEquals(formatter.format(firstDate.plusDays(9)), purchaseStats.getTo());
        assertEquals(10, purchaseStats.getCountPurchases());
        assertEquals(100.0, purchaseStats.getTotalAmount());
        assertEquals(10.0, purchaseStats.getAvgAmount());
        assertEquals(10.0, purchaseStats.getMinAmount());
        assertEquals(10.0, purchaseStats.getMaxAmount());
    }
}
