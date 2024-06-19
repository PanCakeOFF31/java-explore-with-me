package ru.practicum;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.exception.InvalidDurationException;
import ru.practicum.model.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StatsServiceImpl implements StatsService {
    private final StatRepository statRepository;
    private final AppRepository appRepository;

    @Override
    @Transactional
    public void toHit(StatRequestDto requestDto) {
        log.debug("StatsServiceImpl - service.toHit({})", requestDto);

        String appName = requestDto.getApp();
        Optional<App> foundedApp = appRepository.findByApp(appName);
        App app = App.builder().app(appName).build();

        if (foundedApp.isEmpty()) {
            app = appRepository.save(app);
        } else {
            app = foundedApp.get();
        }

        Stat stat = StatMapper.mapToStat(requestDto);
        stat.setApp(app);
        statRepository.save(stat);
    }

    @Override
    public List<StatResponseDto> getStats(String start, String end, String[] uris, boolean unique) {
        log.debug("StatsServiceImpl - service.getStats({}, {}, {}, {})", start, end, uris, unique);

        LocalDateTime startTime = LocalDateTime.parse(start, StatMapper.dateTimeFormatter);
        LocalDateTime endTime = LocalDateTime.parse(end, StatMapper.dateTimeFormatter);

        if (endTime.isBefore(startTime)) {
            String message = "endTime " + endTime + " должен быть после startTime " + startTime;
            log.warn(message);
            throw new InvalidDurationException(message);
        }

        if (uris.length == 0 && unique) {
            log.info("Запрос на выборку где uris - пустой и ip - уникальные");
            return StatMapper.mapToStatResponseViewDto(statRepository
                    .findAllUniqueByRequestedBetween(startTime, endTime));
        }

        if (uris.length == 0) {
            log.info("Запрос на выборку где uris - пустой и ip - неуникальные");
            return StatMapper.mapToStatResponseViewDto(statRepository
                    .findAllByRequestedBetweenOrderByRequestedDesc(startTime, endTime));
        }

        if (unique) {
            log.info("Запрос на выборку где uris - не пустой и ip - уникальные");
            return StatMapper.mapToStatResponseViewDto(statRepository
                    .findAllUniqueByRequestedBetweenAndUriIn(startTime, endTime, uris));
        }

        log.info("Запрос на выборку где uris - не пустой и ip - неуникальные");
        return StatMapper.mapToStatResponseViewDto(statRepository
                .findAllByRequestedBetweenAndUriInOrderByRequestedDesc(startTime, endTime, uris));
    }
}