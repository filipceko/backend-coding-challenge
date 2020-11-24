package com.journi.challenge.controllers;

import com.journi.challenge.CurrencyConverter;
import com.journi.challenge.models.Product;
import com.journi.challenge.repositories.ProductsRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.util.LinkedList;
import java.util.List;

@RestController
public class ProductsController {
    @Inject
    private CurrencyConverter currencyConverter;
    @Inject
    private ProductsRepository productsRepository;

    @GetMapping("/products")
    public List<Product> list(@RequestParam(name = "countryCode", defaultValue = "AT") String countryCode) {
        List<Product> products = productsRepository.list();
        String currencyCode = currencyConverter.getCurrencyForCountryCode(countryCode);
        //Localize product prices
        List<Product> requiredList = new LinkedList<>();
        products.forEach(product -> {
            Product localizedProduct = new Product(
                    product.getId(),
                    product.getDescription(),
                    currencyConverter.convertEurToCurrency(currencyCode, product.getPrice())); // convert currency
            requiredList.add(localizedProduct);
        });
        return requiredList;
    }
}
