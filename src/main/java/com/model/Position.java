package com.model;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Position (Job) entity class
 */
public class Position implements Serializable {
    private int positionId;
    private String positionTitle;
    private BigDecimal minSalary;
    private BigDecimal maxSalary;
    
    public Position() {
    }
    
    public Position(String positionTitle, BigDecimal minSalary, BigDecimal maxSalary) {
        this.positionTitle = positionTitle;
        this.minSalary = minSalary;
        this.maxSalary = maxSalary;
    }
    
    public Position(int positionId, String positionTitle, BigDecimal minSalary, BigDecimal maxSalary) {
        this.positionId = positionId;
        this.positionTitle = positionTitle;
        this.minSalary = minSalary;
        this.maxSalary = maxSalary;
    }
    
    public int getPositionId() {
        return positionId;
    }
    
    public void setPositionId(int positionId) {
        this.positionId = positionId;
    }
    
    public String getPositionTitle() {
        return positionTitle;
    }
    
    public void setPositionTitle(String positionTitle) {
        this.positionTitle = positionTitle;
    }
    
    public BigDecimal getMinSalary() {
        return minSalary;
    }
    
    public void setMinSalary(BigDecimal minSalary) {
        this.minSalary = minSalary;
    }
    
    public BigDecimal getMaxSalary() {
        return maxSalary;
    }
    
    public void setMaxSalary(BigDecimal maxSalary) {
        this.maxSalary = maxSalary;
    }
    
    @Override
    public String toString() {
        return "Position{" +
                "positionId=" + positionId +
                ", positionTitle='" + positionTitle + '\'' +
                ", minSalary=" + minSalary +
                ", maxSalary=" + maxSalary +
                '}';
    }
}