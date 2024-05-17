package ru.babenko.mapppers;

import org.mapstruct.Mapper;
import ru.babenko.dto.banks.FullBankDto;
import ru.babenko.entities.Bank;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BanksMapper {
    FullBankDto toFullBankDto(Bank bank);

    List<FullBankDto> toFullBankDtoList(List<Bank> banks);
}
