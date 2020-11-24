package com.journi.challenge.repositories;

import com.journi.challenge.models.Purchase;
import javax.inject.Named;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;

@Named
@Singleton
public class PurchasesRepository {

    private final List<Purchase> allPurchases = new ArrayList<>();

    public List<Purchase> list() {
        return allPurchases;
    }

    public void save(Purchase purchase) {
        allPurchases.add(purchase);
    }

    /* I believe the bugs in the statistics computation were that it crashed with no data and that
     it returned min value for both min and max fields.
     */
}
