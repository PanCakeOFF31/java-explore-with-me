package ru.practicum.event.dto;

import lombok.RequiredArgsConstructor;
import ru.practicum.category.dto.CategoryMapper;
import ru.practicum.event.model.Event;
import ru.practicum.location.model.LocationMapper;
import ru.practicum.user.dto.UserMapper;

import java.util.ArrayList;
import java.util.List;

import static ru.practicum.common.component.CommonComponentImpl.DTF;

@RequiredArgsConstructor
public class EventMapper {

    public static Event mapToEvent(NewEventDto newEventDto) {
        return Event.builder()
                .title(newEventDto.getTitle())
                .annotation(newEventDto.getAnnotation())
                .description(newEventDto.getDescription())
                .eventDate(newEventDto.getEventDate())
                .paid(newEventDto.getPaid())
                .participantLimit(newEventDto.getParticipantLimit())
                .requestModeration(newEventDto.getRequestModeration())
                .build();
    }

    public static Event mapToEvent(UpdateEventAdminRequest updateEventDto) {
        return Event.builder()
                .title(updateEventDto.getTitle())
                .annotation(updateEventDto.getAnnotation())
                .description(updateEventDto.getDescription())
                .eventDate(updateEventDto.getEventDate())
                .requestModeration(updateEventDto.getRequestModeration())
                .participantLimit(updateEventDto.getParticipantLimit())
                .paid(updateEventDto.getPaid())
                .build();
    }

    public static Event mapToEvent(UpdateEventUserRequest updateEventDto) {
        return Event.builder()
                .title(updateEventDto.getTitle())
                .annotation(updateEventDto.getAnnotation())
                .description(updateEventDto.getDescription())
                .eventDate(updateEventDto.getEventDate())
                .requestModeration(updateEventDto.getRequestModeration())
                .participantLimit(updateEventDto.getParticipantLimit())
                .paid(updateEventDto.getPaid())
                .build();
    }

    public static EventShortDto mapToEventShortDto(Event event) {
        return EventShortDto.builder()
                .id(event.getId())
                .category(CategoryMapper.mapToCategoryDto(event.getCategory()))
                .initiator(UserMapper.mapToUserShortDto(event.getInitiator()))
                .title(event.getTitle())
                .annotation(event.getAnnotation())
                .eventDate(DTF.format(event.getEventDate()))
                .paid(event.getPaid())
                .confirmedRequests(event.getConfirmedRequests())
                .views(event.getViews())
                .likes(event.getLikes())
                .dislikes(event.getDislikes())
                .rating(event.getRating())
                .build();
    }

    public static List<EventShortDto> mapToEventShortDto(Iterable<Event> events) {
        List<EventShortDto> dtos = new ArrayList<>();

        for (Event event : events) {
            dtos.add(mapToEventShortDto(event));
        }

        return dtos;
    }

    public static EventFullDto mapToEventFullDto(Event event) {
        String publishedOn = null;

        if (event.getPublishedOn() != null)
            publishedOn = DTF.format(event.getPublishedOn());

        return EventFullDto.builder()
                .id(event.getId())
                .location(LocationMapper.mapToLocationDto(event.getLocation()))
                .category(CategoryMapper.mapToCategoryDto(event.getCategory()))
                .initiator(UserMapper.mapToUserShortDto(event.getInitiator()))
                .title(event.getTitle())
                .annotation(event.getAnnotation())
                .description(event.getDescription())
                .createdOn(DTF.format(event.getCreatedOn()))
                .eventDate(DTF.format(event.getEventDate()))
                .publishedOn(publishedOn)
                .participantLimit(event.getParticipantLimit())
                .requestModeration(event.getRequestModeration())
                .paid(event.getPaid())
                .confirmedRequests(event.getConfirmedRequests())
                .state(event.getState())
                .views(event.getViews())
                .likes(event.getLikes())
                .dislikes(event.getDislikes())
                .rating(event.getRating())
                .build();
    }

    public static List<EventFullDto> mapToEventFullDto(Iterable<Event> events) {
        List<EventFullDto> dtos = new ArrayList<>();

        for (Event event : events) {
            dtos.add(mapToEventFullDto(event));
        }

        return dtos;
    }
}
