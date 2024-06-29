package ru.practicum.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.UpdateEventAdminRequest;
import ru.practicum.event.model.EventState;
import ru.practicum.event.service.EventService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/events")
public class AdminEventController {
    private final EventService eventService;

    /**
     * <h5>Поиск событий</h5>
     * <p>Эндпоинт возвращает полную информацию обо всех событиях подходящих под переданные условия.
     * В случае, если по заданным фильтрам не найдено ни одного события, возвращает пустой список</p>
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<EventFullDto> fetchAdminEvents(@RequestParam(required = false) List<Long> users,
                                               @RequestParam(required = false) List<EventState> states,
                                               @RequestParam(required = false) List<Long> categories,
                                               @RequestParam(required = false) LocalDateTime rangeStart,
                                               @RequestParam(required = false) LocalDateTime rangeEnd,
                                               @RequestParam(defaultValue = "0") @PositiveOrZero final int from,
                                               @RequestParam(defaultValue = "10") @Positive final int size) {
        log.debug("/admin/events - GET: fetchAdminEvents(users={},\nstates={},\ncategories={},\nrangeStart={}," +
                        " rangeEnd={}, from={}, size={})",
                users, states, categories, rangeStart, rangeEnd, from, size);
        return eventService.fetchAdminEvents(
                users, states, categories, rangeStart, rangeEnd, from, size);
    }

    /**
     * <h5>Редактирование данных любого события администратором. Валидация данных не требуется</h5>
     * <p>Обратите внимание:</p>
     * <ul>
     *     <li>дата начала изменяемого события должна быть не ранее чем за час от даты публикации. (Ожидается код ошибки 409)</li>
     *     <li>событие можно публиковать, только если оно в состоянии ожидания публикации (Ожидается код ошибки 409)</li>
     *     <li>событие можно отклонить, только если оно еще не опубликовано (Ожидается код ошибки 409)</li>
     *     <li>Нельзя редактировать событие, которое началось или прошло</li>
     *
     * </ul>
     */
    @PatchMapping("/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto updateAdminEvent(@PathVariable long eventId,
                                         @RequestBody @Valid UpdateEventAdminRequest updateRequest) {
        log.debug("/admin/events/{} - PATCH: updateAdminEvent({}, {})", eventId, eventId, updateRequest);
        return eventService.updateAdminEvent(eventId, updateRequest);
    }
}