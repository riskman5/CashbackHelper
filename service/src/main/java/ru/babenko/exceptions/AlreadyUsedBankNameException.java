package ru.babenko.exceptions;

public class AlreadyUsedBankNameException extends RuntimeException {
    public AlreadyUsedBankNameException(String name) {
        super(name);
    }

    @Override
    public String getMessage() {
        return "Bank with name " + super.getMessage() + " already exists";
    }
}
