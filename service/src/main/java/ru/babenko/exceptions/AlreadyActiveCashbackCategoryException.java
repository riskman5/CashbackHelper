package ru.babenko.exceptions;

public class AlreadyActiveCashbackCategoryException extends RuntimeException {
    public AlreadyActiveCashbackCategoryException(String name) {
        super(name);
    }

    @Override
    public String getMessage() {
        return "Cashback category with name " + super.getMessage() + " is already active";
    }
}
