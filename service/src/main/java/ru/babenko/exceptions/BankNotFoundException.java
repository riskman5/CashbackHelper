package ru.babenko.exceptions;

public class BankNotFoundException extends RuntimeException {
    public BankNotFoundException(String name) {
        super(name);
    }

    @Override
    public String getMessage() {
        return "Bank with name " + super.getMessage() + " not found";
    }
}
