package com.model;

import java.io.Serializable;

/**
 * City entity class
 */
public class City implements Serializable {
    private int cityId;
    private String cityName;
    private int countryId;
    private Country country; // For reference, not stored in DB
    
    public City() {
    }
    
    public City(String cityName, int countryId) {
        this.cityName = cityName;
        this.countryId = countryId;
    }
    
    public City(int cityId, String cityName, int countryId) {
        this.cityId = cityId;
        this.cityName = cityName;
        this.countryId = countryId;
    }
    
    public int getCityId() {
        return cityId;
    }
    
    public void setCityId(int cityId) {
        this.cityId = cityId;
    }
    
    public String getCityName() {
        return cityName;
    }
    
    public void setCityName(String cityName) {
        this.cityName = cityName;
    }
    
    public int getCountryId() {
        return countryId;
    }
    
    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }
    
    public Country getCountry() {
        return country;
    }
    
    public void setCountry(Country country) {
        this.country = country;
        if (country != null) {
            this.countryId = country.getCountryId();
        }
    }
    
    @Override
    public String toString() {
        return "City{" +
                "cityId=" + cityId +
                ", cityName='" + cityName + '\'' +
                ", countryId=" + countryId +
                '}';
    }
}