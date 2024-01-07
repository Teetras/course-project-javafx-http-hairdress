package org.example;

public class Configs {
    protected String dbHost ="localhost";
    protected String dbPort="3306";
    protected String dbUser="root";
    protected String dbPass="1234";
    protected String dbName="course";

    public String getdbHost() {
        return System.getProperty("dbHost", dbHost);
    }

    public String getDbPass() {
        return System.getProperty("dbPass", dbPass);
    }
    public String getDbUser() {
        return System.getProperty("dbUser", dbUser);
    }
    public String getdbPort() {
        return System.getProperty("dbPort", dbPort);
    }
    public String getdbName() {
        return System.getProperty("dbName", dbName);
    }
}