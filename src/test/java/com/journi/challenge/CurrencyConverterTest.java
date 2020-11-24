package com.journi.challenge;

import org.junit.jupiter.api.Test;
import org.springframework.stereotype.Component;

import static org.junit.jupiter.api.Assertions.*;

@Component
class CurrencyConverterTest {

    private CurrencyConverter currencyConverter = new CurrencyConverter();

    @Test
    void findCurrencyCodeForSupportedCountry() {
        assertEquals("EUR", currencyConverter.getCurrencyForCountryCode("AT"));
        assertEquals("EUR", currencyConverter.getCurrencyForCountryCode("DE"));
        assertEquals("EUR", currencyConverter.getCurrencyForCountryCode("FR"));
        assertEquals("BRL", currencyConverter.getCurrencyForCountryCode("BR"));
        assertEquals("GBP", currencyConverter.getCurrencyForCountryCode("GB"));
    }

    @Test
    void findCurrencyCodeForNonSupportedCountry() {
        assertEquals("EUR", currencyConverter.getCurrencyForCountryCode("CH"));
        assertEquals("EUR", currencyConverter.getCurrencyForCountryCode("CL"));
        assertEquals("EUR", currencyConverter.getCurrencyForCountryCode("AR"));
        assertEquals("EUR", currencyConverter.getCurrencyForCountryCode("FI"));
    }

    @Test
    void convertEurValueToSupportedCurrency() {
        assertEquals(25.0, currencyConverter.convertEurToCurrency("EUR", 25.0));
        assertEquals(25.0 * 5.1480, currencyConverter.convertEurToCurrency("BRL", 25.0));
        assertEquals(25.5, currencyConverter.convertEurToCurrency("WTV", 25.5));
    }

    @Test
    void convertSupportedCurrencyToEur(){
        assertEquals(500 / 119.63, currencyConverter.convertCurrencyToEur("JPY", 500.0));
        assertEquals(500, currencyConverter.convertCurrencyToEur("EUR", 500.0));
        assertEquals(25.5, currencyConverter.convertCurrencyToEur("WTV", 25.5));
    }
}