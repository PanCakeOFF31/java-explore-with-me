package ru.practicum.location.service;

import ru.practicum.location.dto.LocationDto;
import ru.practicum.location.model.Location;

public interface LocationService {
    Location getOrElseCreateLocation(LocationDto location);

    void deleteIfUnusedLocation(long locationId);
}
