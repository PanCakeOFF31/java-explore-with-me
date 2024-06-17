package ru.practicum.model;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class StatMapper {
    public static Stat mapToStat(StatRequestDto statHitRequestDto) {
        return Stat.builder()
                .uri(statHitRequestDto.getUri())
                .ip(statHitRequestDto.getIp())
                .requested(statHitRequestDto.getTimestamp())
                .build();
    }

    public static StatResponseDto mapToStatResponseDto(Stat stat) {
        return StatResponseDto.builder()
                .uri(stat.getUri())
                .build();
    }

    public static List<StatResponseDto> mapToStatResponseDto(final Iterable<Stat> items) {
        List<StatResponseDto> dtos = new ArrayList<>();

        for (Stat item : items) {
            dtos.add(mapToStatResponseDto(item));
        }

        return dtos;
    }

    public static StatResponseDto mapToStatResponseViewDto(StatResponseViewDto stat) {
        return StatResponseDto.builder()
                .app(stat.getApp())
                .uri(stat.getUri())
                .hits(stat.getHits())
                .build();
    }

    public static List<StatResponseDto> mapToStatResponseViewDto(final Iterable<StatResponseViewDto> items) {
        List<StatResponseDto> dtos = new ArrayList<>();

        for (StatResponseViewDto item : items) {
            dtos.add(mapToStatResponseViewDto(item));
        }

        return dtos;
    }

    public static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
}