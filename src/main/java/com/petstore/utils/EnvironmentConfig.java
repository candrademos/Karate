package com.petstore.utils;


public class EnvironmentConfig {
    public String getVariable(String variable) {
        String value = System.getenv(variable);
        return value == null ? "" : value;
    }

}