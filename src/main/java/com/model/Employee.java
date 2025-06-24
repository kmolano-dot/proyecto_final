package com.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;

/**
 * Employee entity class
 */
public class Employee implements Serializable {
    private int employeeId;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private Date hireDate;
    private int positionId;
    private BigDecimal salary;
    private int departmentId;
    private boolean isDeleted;
    
    // For reference, not stored in DB
    private Position position;
    private Department department;
    
    public Employee() {
    }
    
    public Employee(String firstName, String lastName, String email, String phoneNumber, 
                   Date hireDate, int positionId, BigDecimal salary, int departmentId) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.hireDate = hireDate;
        this.positionId = positionId;
        this.salary = salary;
        this.departmentId = departmentId;
        this.isDeleted = false;
    }
    
    public Employee(int employeeId, String firstName, String lastName, String email, 
                   String phoneNumber, Date hireDate, int positionId, BigDecimal salary, 
                   int departmentId, boolean isDeleted) {
        this.employeeId = employeeId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.hireDate = hireDate;
        this.positionId = positionId;
        this.salary = salary;
        this.departmentId = departmentId;
        this.isDeleted = isDeleted;
    }
    
    public int getEmployeeId() {
        return employeeId;
    }
    
    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }
    
    public String getFirstName() {
        return firstName;
    }
    
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    
    public String getLastName() {
        return lastName;
    }
    
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPhoneNumber() {
        return phoneNumber;
    }
    
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    
    public Date getHireDate() {
        return hireDate;
    }
    
    public void setHireDate(Date hireDate) {
        this.hireDate = hireDate;
    }
    
    public int getPositionId() {
        return positionId;
    }
    
    public void setPositionId(int positionId) {
        this.positionId = positionId;
    }
    
    public BigDecimal getSalary() {
        return salary;
    }
    
    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }
    
    public int getDepartmentId() {
        return departmentId;
    }
    
    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }
    
    public boolean isDeleted() {
        return isDeleted;
    }
    
    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }
    
    public Position getPosition() {
        return position;
    }
    
    public void setPosition(Position position) {
        this.position = position;
        if (position != null) {
            this.positionId = position.getPositionId();
        }
    }
    
    public Department getDepartment() {
        return department;
    }
    
    public void setDepartment(Department department) {
        this.department = department;
        if (department != null) {
            this.departmentId = department.getDepartmentId();
        }
    }
    
    @Override
    public String toString() {
        return "Employee{" +
                "employeeId=" + employeeId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", hireDate=" + hireDate +
                ", positionId=" + positionId +
                ", salary=" + salary +
                ", departmentId=" + departmentId +
                ", isDeleted=" + isDeleted +
                '}';
    }
}