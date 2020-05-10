package me.P3ntest.permissions.time;

public class InvalidDateException extends Exception {
    private String error;

    public InvalidDateException(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }
}
