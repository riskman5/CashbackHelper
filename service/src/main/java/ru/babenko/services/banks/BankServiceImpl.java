package ru.babenko.services.banks;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.babenko.dao.BanksRepository;
import ru.babenko.dto.banks.FullBankDto;
import ru.babenko.dto.banks.InitialBankDto;
import ru.babenko.entities.Bank;
import ru.babenko.exceptions.AlreadyUsedBankNameException;
import ru.babenko.mapppers.BanksMapper;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BankServiceImpl implements BankService {
    private final BanksRepository bankRepository;
    private final BanksMapper banksMapper;

    @Override
    public FullBankDto addBank(InitialBankDto initialBankDto) {
        if (bankRepository.existsByName(initialBankDto.name())) {
            throw new AlreadyUsedBankNameException(initialBankDto.name());
        }

        Bank newBank = Bank.builder()
            .name(initialBankDto.name())
            .cashbackLimit(initialBankDto.cashBackLimit())
            .cards(List.of())
            .build();
        newBank = bankRepository.save(newBank);

        return banksMapper.toFullBankDto(newBank);
    }
}
