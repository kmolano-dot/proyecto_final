package com.model;

import java.io.Serializable;

/**
 * Location entity class
 */
public class Location implements Serializable {
    private int locationId;
    private String streetAddress;
    private String postalCode;
    private int cityId;
    private City city; // For reference, not stored in DB
    
    public Location() {
    }
    
    public Location(String streetAddress, String postalCode, int cityId) {
        this.streetAddress = streetAddress;
        this.postalCode = postalCode;
        this.cityId = cityId;
    }
    
    public Location(int locationId, String streetAddress, String postalCode, int cityId) {
        this.locationId = locationId;
        this.streetAddress = streetAddress;
        this.postalCode = postalCode;
        this.cityId = cityId;
    }
    
    public int getLocationId() {
        return locationId;
    }
    
    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }
    
    public String getStreetAddress() {
        return streetAddress;
    }
    
    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }
    
    public String getPostalCode() {
        return postalCode;
    }
    
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }
    
    public int getCityId() {
        return cityId;
    }
    
    public void setCityId(int cityId) {
        this.cityId = cityId;
    }
    
    public City getCity() {
        return city;
    }
    
    public void setCity(City city) {
        this.city = city;
        if (city != null) {
            this.cityId = city.getCityId();
        }
    }
    
    @Override
    public String toString() {
        return "Location{" +
                "locationId=" + locationId +
                ", streetAddress='" + streetAddress + '\'' +
                ", postalCode='" + postalCode + '\'' +
                ", cityId=" + cityId +
                '}';
    }
}