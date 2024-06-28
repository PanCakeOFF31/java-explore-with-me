package ru.practicum.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.model.EventSort;
import ru.practicum.event.service.EventService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/events")
public class PublicEventController {
    private final EventService eventService;

    /**
     * <h5>Получение событий с возможностью фильтрации</h5>
     * <p>Обратите внимание</p>
     * <ul>
     * <li>это публичный эндпоинт, соответственно в выдаче должны быть только опубликованные события</li>
     * <li>текстовый поиск (по аннотации и подробному описанию) должен быть без учета регистра букв</li>
     * <li>если в запросе не указан диапазон дат [rangeStart-rangeEnd], то нужно выгружать события, которые произойдут позже текущей даты и времени</li>
     * <li>информация о каждом событии должна включать в себя количество просмотров и количество уже одобренных заявок на участие</li>
     * <li>информацию о том, что по этому эндпоинту был осуществлен и обработан запрос, нужно сохранить в сервисе статистики</li>
     * </ul>
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<EventShortDto> fetchPublicEvents(@RequestParam(required = false) String text,
                                                 @RequestParam(required = false) List<Long> categories,
                                                 @RequestParam(required = false) Boolean paid,
                                                 @RequestParam(required = false) LocalDateTime rangeStart,
                                                 @RequestParam(required = false) LocalDateTime rangeEnd,
                                                 @RequestParam(required = false) Boolean onlyAvailable,
                                                 @RequestParam(required = false) EventSort sort,
                                                 @RequestParam(defaultValue = "0") @PositiveOrZero final int from,
                                                 @RequestParam(defaultValue = "10") @Positive final int size,
                                                 HttpServletRequest servletRequest) {
        log.debug("/events - GET: fetchPublicEvents(text={},categories={}, {},\nrangeStart={}," +
                        " rangeEnd={}, {}, {}, from={}, size={})",
                text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size);
        return eventService.fetchPublicEvents(
                text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size, servletRequest);
    }

    /**
     * <h5>Получение подробной информации об опубликованном событии по его идентификатору</h5>
     * <p>Обратите внимание</p>
     * <ul>
     * <li>cобытие должно быть опубликовано</li>
     * <li>информация о событии должна включать в себя количество просмотров и количество подтвержденных запросов</li>
     * <li>информацию о том, что по этому эндпоинту был осуществлен и обработан запрос, нужно сохранить в сервисе статистики</li>
     * </ul>
     */
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto fetchPublicEventById(@PathVariable(name = "id") long eventId,
                                             HttpServletRequest servletRequest) {
        log.debug("/events/{} - GET: fetchPublicEventById({})", eventId, eventId);
        return eventService.fetchPublicEventById(eventId, servletRequest);
    }
}
