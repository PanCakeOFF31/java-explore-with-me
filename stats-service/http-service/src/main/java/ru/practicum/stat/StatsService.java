package ru.practicum.stat;


import ru.practicum.model.stat.dto.StatRequestDto;
import ru.practicum.model.stat.dto.StatResponseDto;

import java.util.List;

public interface StatsService {
    void toHit(StatRequestDto requestDto);

    List<StatResponseDto> getStats(String start,
                                   String end,
                                   String[] uris,
                                   boolean unique);
}
