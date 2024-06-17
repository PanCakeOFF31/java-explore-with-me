package ru.practicum;


import ru.practicum.model.StatRequestDto;
import ru.practicum.model.StatResponseDto;

import java.util.List;

public interface StatsService {
    void toHit(StatRequestDto requestDto);

    List<StatResponseDto> getStats(String start,
                                   String end,
                                   String[] uris,
                                   boolean unique);
}
