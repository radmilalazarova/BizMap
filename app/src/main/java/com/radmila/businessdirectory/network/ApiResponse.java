package com.radmila.businessdirectory.network;

public class ApiResponse {
    private boolean success;
    private int id;
    private String message;

    public boolean isSuccess() { return success; }
    public int getId() { return id; }
    public String getMessage() { return message; }
}