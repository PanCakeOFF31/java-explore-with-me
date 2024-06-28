package ru.practicum;

import org.springframework.http.ResponseEntity;
import ru.practicum.model.stat.dto.StatRequestDto;

public interface StatClient {

    ResponseEntity<Object> toHit(StatRequestDto statRequestDto);

    ResponseEntity<Object> getStats(String start, String end);

    ResponseEntity<Object> getStats(String start, String end, String[] uris);

    ResponseEntity<Object> getStats(String start, String end, boolean unique);

    ResponseEntity<Object> getStats(String start, String end, String[] uris, boolean unique);

    long getUniqueEventViews(long eventId);
}
