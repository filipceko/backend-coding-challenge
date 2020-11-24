package com.journi.challenge.repositories;

import com.journi.challenge.models.Purchase;
import com.journi.challenge.models.PurchaseStats;

import javax.inject.Named;
import javax.inject.Singleton;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

/*
The generation of the statistics itself is in O(1) (all operations are constant).
The add and remove methods, however, are in worse shape add with O(ln(n)) due to sorting based on price and
remove can be even O(n*ln(n)) as all outdated purchases need to be removed (can be up to n).
Remove can be triggered daily at night when the traffic is lower.
 */

/**
 * Class handling just the records needed to compute last month statistics
 */
@Named
@Singleton
public class StatisticsRepository {
    /**
     * Date formatter
     */
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_DATE.withZone(ZoneId.of("UTC"));

    /**
     * Relevant purchases to the last updated statistics ordered by date
     */
    private final LinkedList<Purchase> relevantPurchases = new LinkedList<>();

    /**
     * Relevant purchases to the last updated statistics ordered by price
     */
    private final TreeMap<Double, Integer> minTree = new TreeMap<>();

    long size; //If list is longer then INT_MAX, we have to keep track
    double totalPrice;


    public void addPurchase(Purchase purchase) {
        relevantPurchases.add(purchase);
        ++size;
        totalPrice += purchase.getTotalValue();
        if(minTree.containsKey(purchase.getTotalValue())){
            minTree.put(purchase.getTotalValue(), minTree.get(purchase.getTotalValue()) + 1);
        } else {
            minTree.put(purchase.getTotalValue(), 1);
        }
        removeOutdatedPurchases();
    }

    /**
     * Remove all outdated (older then 30 days) purchases from the statistics
     */
    private void removeOutdatedPurchases() {
        if (relevantPurchases.isEmpty()) {
            return;
        }
        LocalDateTime start = LocalDate.now().atStartOfDay().minusDays(30);
        Iterator<Purchase> iterator = relevantPurchases.iterator();
        Purchase current = iterator.next();
        while (current != null && current.getTimestamp().isBefore(start)) {
            iterator.remove();
            --size;
            totalPrice -= current.getTotalValue();
            minTree.put(current.getTotalValue(), minTree.get(current.getTotalValue()) - 1);
            if (minTree.get(current.getTotalValue()) <= 0) {
                minTree.remove(current.getTotalValue());
            }
            if (iterator.hasNext()){
                current = iterator.next();
            } else {
                break;
            }
        }
    }

    public PurchaseStats getStatistics(){
        removeOutdatedPurchases(); //Validate data
        if (relevantPurchases.isEmpty()){
            //TODO Throw an error/log/special action?
            return null;
        }
        return new PurchaseStats(
                FORMATTER.format(relevantPurchases.getFirst().getTimestamp()),
                FORMATTER.format(relevantPurchases.getLast().getTimestamp()),
                size,
                totalPrice,
                totalPrice / size,
                minTree.descendingKeySet().last(),
                minTree.descendingKeySet().first()
        );
    }
}
