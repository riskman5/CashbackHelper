package ru.babenko.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.babenko.dto.banks.FullBankDto;
import ru.babenko.dto.banks.InitialBankDto;
import ru.babenko.services.banks.BankService;

@RestController
@RequestMapping("/api/v1/banks")
@RequiredArgsConstructor
public class BanksController {
    private final BankService bankService;

    @PostMapping
    public FullBankDto addBank(@Valid @RequestBody InitialBankDto initialBankDto) {
        return bankService.addBank(initialBankDto);
    }
}
