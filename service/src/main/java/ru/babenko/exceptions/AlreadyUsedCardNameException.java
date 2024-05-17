package ru.babenko.exceptions;

public class AlreadyUsedCardNameException extends RuntimeException {
    public AlreadyUsedCardNameException(String name) {
        super(name);
    }

    @Override
    public String getMessage() {
        return "Card with name " + super.getMessage() + " already exists";
    }
}
