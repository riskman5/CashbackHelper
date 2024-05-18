package ru.babenko.exceptionsHandlers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.babenko.exceptions.AlreadyUsedBankNameException;
import ru.babenko.exceptions.BankNotFoundException;


@RestControllerAdvice
public class BanksExceptionsHandler {
    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(AlreadyUsedBankNameException.class)
    public String handleAlreadyUsedBankName(AlreadyUsedBankNameException e) {
        return e.getMessage();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BankNotFoundException.class)
    public String handleBankNotFound(BankNotFoundException e) {
        return e.getMessage();
    }
}
