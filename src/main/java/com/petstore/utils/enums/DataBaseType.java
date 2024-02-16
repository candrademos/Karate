package com.petstore.utils.enums;


import lombok.Getter;

@Getter
public enum DataBaseType {

    SQLSERVER("com.microsoft.sqlserver.jdbc.SQLServerDriver","jdbc:sqlserver://<host>:<port>;databaseName=<database>;encrypt=false"),


    private final String driver;
    private final String jdbcUrl;
    DataBaseType(String driver, String jdbcUrl) {
        this.driver = driver;
        this.jdbcUrl = jdbcUrl;
    }

}
