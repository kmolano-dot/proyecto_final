package com.model;

import java.io.Serializable;

/**
 * Country entity class
 */
public class Country implements Serializable {
    private int countryId;
    private String countryName;
    
    public Country() {
    }
    
    public Country(String countryName) {
        this.countryName = countryName;
    }
    
    public Country(int countryId, String countryName) {
        this.countryId = countryId;
        this.countryName = countryName;
    }
    
    public int getCountryId() {
        return countryId;
    }
    
    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }
    
    public String getCountryName() {
        return countryName;
    }
    
    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }
    
    @Override
    public String toString() {
        return "Country{" +
                "countryId=" + countryId +
                ", countryName='" + countryName + '\'' +
                '}';
    }
}