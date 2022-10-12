package org.converter;

public class Rates {
    private String effectiveDate;
    private double mid;

    private Rates(String effectiveDate, double mid) {
        this.effectiveDate = effectiveDate;
        this.mid = mid;
    }

    public String getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(String effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public double getMid() {
        return mid;
    }

    public void setMid(double mid) {
        this.mid = mid;
    }

}
