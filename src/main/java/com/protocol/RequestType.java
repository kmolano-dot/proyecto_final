package com.protocol;

/**
 * Enum defining the types of requests that can be sent from client to server
 */
public enum RequestType {
    INSERT_COUNTRY,
    INSERT_CITY,
    INSERT_LOCATION,
    INSERT_DEPARTMENT,
    INSERT_POSITION,
    INSERT_EMPLOYEE,
    
    UPDATE_COUNTRY,
    UPDATE_CITY,
    UPDATE_LOCATION,
    UPDATE_DEPARTMENT,
    UPDATE_POSITION,
    UPDATE_EMPLOYEE,
    
    SELECT_COUNTRY,
    SELECT_CITY,
    SELECT_LOCATION,
    SELECT_DEPARTMENT,
    SELECT_POSITION,
    SELECT_EMPLOYEE,
    
    DELETE_EMPLOYEE
}