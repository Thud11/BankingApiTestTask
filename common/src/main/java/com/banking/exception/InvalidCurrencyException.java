package com.banking.exception;

public class InvalidCurrencyException extends RuntimeException {
    public InvalidCurrencyException(String currency) {
        super("Invalid currency: " + currency + ". Allowed values: USD, EUR, GBP, UAH");
    }
}

