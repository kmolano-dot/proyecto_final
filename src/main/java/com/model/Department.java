package com.model;

import java.io.Serializable;

/**
 * Department entity class
 */
public class Department implements Serializable {
    private int departmentId;
    private String departmentName;
    private int locationId;
    private Location location; // For reference, not stored in DB
    
    public Department() {
    }
    
    public Department(String departmentName, int locationId) {
        this.departmentName = departmentName;
        this.locationId = locationId;
    }
    
    public Department(int departmentId, String departmentName, int locationId) {
        this.departmentId = departmentId;
        this.departmentName = departmentName;
        this.locationId = locationId;
    }
    
    public int getDepartmentId() {
        return departmentId;
    }
    
    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }
    
    public String getDepartmentName() {
        return departmentName;
    }
    
    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }
    
    public int getLocationId() {
        return locationId;
    }
    
    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }
    
    public Location getLocation() {
        return location;
    }
    
    public void setLocation(Location location) {
        this.location = location;
        if (location != null) {
            this.locationId = location.getLocationId();
        }
    }
    
    @Override
    public String toString() {
        return "Department{" +
                "departmentId=" + departmentId +
                ", departmentName='" + departmentName + '\'' +
                ", locationId=" + locationId +
                '}';
    }
}