package org.converter;

import jakarta.xml.bind.annotation.*;

@XmlType(propOrder = { "name", "exchangeDate", "value_USD", "value_PLN" })
public class Computer {
    private String name;
    private String exchangeDate;
    private double value_USD;
    private double value_PLN;

    public Computer() {}


    public String getName() {
        return name;
    }

    @XmlElement(name = "nazwa")
    public void setName(String name) {
        this.name = name;
    }

    public String getExchangeDate() {
        return exchangeDate;
    }

    @XmlElement(name = "data_ksiegowania")
    public void setExchangeDate(String exchangeDate) {
        this.exchangeDate = exchangeDate;
    }

    public double getValue_USD() {
        return value_USD;
    }

    @XmlElement(name = "koszt_USD")
    public void setValue_USD(double value_USD) {
        this.value_USD = value_USD;
    }

    @XmlElement(name = "koszt_PLN")
    public double getValue_PLN() {
        return value_PLN;
    }

    public void setValue_PLN(double value_PLN) {
        this.value_PLN = value_PLN;
    }
}