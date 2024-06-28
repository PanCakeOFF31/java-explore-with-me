package ru.practicum.partrequest.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.partrequest.dto.EventRequestStatusUpdateRequest;
import ru.practicum.partrequest.dto.EventRequestStatusUpdateResult;
import ru.practicum.partrequest.dto.ParticipationRequestDto;
import ru.practicum.partrequest.service.ParticipationRequestService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class PrivateRequestController {
    private final ParticipationRequestService requestService;

    /**
     * <h5>Получение информации о запросах на участие в событии текущего пользователя</h5>
     * <p>Обратите внимание:</p>
     * <ul>
     *     <li>в случае, если по заданным фильтрам не найдено ни одной заявки, возвращает пустой список</li>
     * </ul>
     */
    @GetMapping("/{userId}/events/{eventId}/requests")
    @ResponseStatus(HttpStatus.OK)
    public List<ParticipationRequestDto> fetchPrivateEventParticRequests(@PathVariable long userId,
                                                                         @PathVariable long eventId) {
        log.debug("/users/{}/events/{}/requests - GET: createPrivateRequest({}, {})", userId, eventId, userId, eventId);
        return requestService.fetchPrivateEventParticRequests(userId, eventId);
    }

    /**
     * <h5>Изменить статус заявок на участие в событии текущего пользователя</h5>
     * <p>Обратите внимание:</p>
     * <ul>
     * <li>если для события лимит заявок равен 0 или отключена пре-модерация заявок, то подтверждение заявок не требуется</li>
     * <li>нельзя подтвердить заявку, если уже достигнут лимит по заявкам на данное событие (Ожидается код ошибки 409)</li>
     * <li>статус можно изменить только у заявок, находящихся в состоянии ожидания (Ожидается код ошибки 409)</li>
     * <li>если при подтверждении данной заявки, лимит заявок для события исчерпан, то все неподтверждённые заявки необходимо отклонить</li>
     * <li>Нельзя редактировать заявки для события, которое началось или прошло</li>
     * </ul>
     */
    @PatchMapping("/{userId}/events/{eventId}/requests")
    @ResponseStatus(HttpStatus.OK)
    public EventRequestStatusUpdateResult modifyPrivateParticRequests(@PathVariable long userId,
                                                                      @PathVariable long eventId,
                                                                      @RequestBody @Valid EventRequestStatusUpdateRequest updateRequest) {
        log.debug("/users/{}/events/{}/requests - PATCH: modifyPrivateParticRequest({}, {}, {})", userId, eventId, userId, eventId, updateRequest);
        return requestService.modifyPrivateParticRequests(userId, eventId, updateRequest);
    }

    /**
     * <h5>Получение информации о заявках текущего пользователя на участие в чужих событиях</h5>
     * <p>Обратите внимание:</p>
     * <ul>
     *     <li>в случае, если по заданным фильтрам не найдено ни одной заявки, возвращает пустой список</li>
     * </ul>
     */
    @GetMapping("/{userId}/requests")
    @ResponseStatus(HttpStatus.OK)
    public List<ParticipationRequestDto> getPrivateUserRequests(@PathVariable long userId) {
        log.debug("/users/{}/requests - GET: getPrivateUserRequests({})", userId, userId);
        return requestService.getPrivateUserRequests(userId);
    }

    /**
     * <h5>Добавление запроса от текущего пользователя на участие в событии</h5>
     * <p>Обратите внимание:</p>
     * <ul>
     * <li>нельзя добавить повторный запрос (Ожидается код ошибки 409)</li>
     * <li>инициатор события не может добавить запрос на участие в своём событии (Ожидается код ошибки 409)</li>
     * <li>нельзя участвовать в неопубликованном событии (Ожидается код ошибки 409)</li>
     * <li>если у события достигнут лимит запросов на участие - необходимо вернуть ошибку (Ожидается код ошибки 409)</li>
     * <li>если для события отключена пре-модерация запросов на участие, то запрос должен автоматически перейти в состояние подтвержденного</li>
     * <li>Нельзя создавать запрос на участие в событии, которое началось или прошло</li>
     * </ul>
     */
    @PostMapping("/{userId}/requests")
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationRequestDto createPrivateRequest(@RequestParam long eventId,
                                                        @PathVariable long userId) {
        log.debug("/users/{}/requests - POST: createPrivateRequest(eventId={}, userId={})", userId, eventId, userId);
        return requestService.createPrivateRequest(eventId, userId);
    }

    /**
     * <h5>Отмена своего запроса на участие в событии</h5>
     * <p>Обратите внимание:</p>
     * <ul>
     *     <li>Нельзя отменять запрос на участие в событии, которое началось или прошло</li>
     * </ul>
     */
    @PatchMapping("/{userId}/requests/{requestId}/cancel")
    @ResponseStatus(HttpStatus.OK)
    public ParticipationRequestDto cancelPrivateSelfRequest(@PathVariable long userId,
                                                            @PathVariable long requestId) {
        log.debug("/users/{}/requests/{}/cancel - PATCH: cancelPrivateSelfRequest({}, {})", userId,
                requestId, userId, requestId);
        return requestService.cancelPrivateSelfRequest(userId, requestId);
    }
}