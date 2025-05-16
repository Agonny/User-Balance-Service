package com.example.userBalanceApp.model;

public interface UniqueData {

    boolean isDataEquals(String qualifier);

    String getQualifier();

    void dropUser();

}
