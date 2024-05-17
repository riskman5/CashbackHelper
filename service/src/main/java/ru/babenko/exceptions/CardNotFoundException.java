package ru.babenko.exceptions;

public class CardNotFoundException extends RuntimeException {
    public CardNotFoundException(String name) {
        super(name);
    }

    @Override
    public String getMessage() {
        return "Card with name " + super.getMessage() + " not found";
    }
}
