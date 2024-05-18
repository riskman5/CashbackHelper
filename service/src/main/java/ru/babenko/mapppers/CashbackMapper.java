package ru.babenko.mapppers;

import org.mapstruct.Mapper;
import ru.babenko.dto.cashbacks.FullCashbackDto;
import ru.babenko.entities.Cashback;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CashbackMapper {
    FullCashbackDto toFullCashbackDto(Cashback cashback);

    List<FullCashbackDto> toFullCashbackDtoList(List<Cashback> cashbacks);
}
