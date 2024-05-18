package ru.babenko.exceptionsHandlers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.babenko.exceptions.AlreadyActiveCashbackCategoryException;
import ru.babenko.exceptions.AlreadyUsedCardNameException;
import ru.babenko.exceptions.CardNotFoundException;

@RestControllerAdvice
public class CardsExceptionsHandler {
    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(AlreadyUsedCardNameException.class)
    public String handleAlreadyUsedCardNameException(AlreadyUsedCardNameException e) {
        return e.getMessage();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(CardNotFoundException.class)
    public String handleCardNotFoundException(CardNotFoundException e) {
        return e.getMessage();
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(AlreadyActiveCashbackCategoryException.class)
    public String handleAlreadyActiveCashbackCategoryException(AlreadyActiveCashbackCategoryException e) {
        return e.getMessage();
    }
}
