package org.converter;

import java.util.List;

public class CurrencyAPI {
    List<Rates> rates;

    private CurrencyAPI(String code, String effectiveDate, double mid) {
        this.rates = rates;
    }

    public List<Rates> getRates(int i) {
        return rates;
    }

    public void setRates(List<Rates> rates) {
        this.rates = rates;
    }
}
