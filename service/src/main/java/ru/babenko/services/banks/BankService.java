package ru.babenko.services.banks;

import ru.babenko.dto.banks.FullBankDto;
import ru.babenko.dto.banks.InitialBankDto;

public interface BankService {
     FullBankDto addBank(InitialBankDto initialBankDto);
}
