package ru.practicum.location.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.common.component.CommonComponent;
import ru.practicum.location.dto.LocationDto;
import ru.practicum.location.model.Location;
import ru.practicum.location.model.LocationMapper;
import ru.practicum.location.repository.LocationRepository;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LocationServiceImpl implements LocationService {
    private final LocationRepository locationRepository;
    private final CommonComponent commonComponent;

    @Override
    @Transactional
    public Location getOrElseCreateLocation(LocationDto location) {
        log.debug("LocationServiceImpl - service.getOrElseCreateLocation({})", location);

        float latitude = location.getLat();
        float longitude = location.getLon();

        if (commonComponent.containsLocationByLatAndLon(latitude, longitude))
            return commonComponent.getLocationByLatAndLon(latitude, longitude);

        Location locationToSave = LocationMapper.mapToLocation(location);
        return locationRepository.save(locationToSave);
    }

    @Override
    @Transactional
    public void deleteIfUnusedLocation(long locationId) {
        log.debug("LocationServiceImpl - service.deleteIfUnusedLocation({})", locationId);

        Optional<Location> usedLocation = locationRepository.findFirstUsedLocation(locationId);
        if (usedLocation.isEmpty()) {
            locationRepository.deleteById(locationId);
            log.info("Выполнилось удаление неиспользуемого местоположения id={}", locationId);
            return;
        }

        log.info("Не выполнилось удаления местоположения id={}, так как оно используется", locationId);
    }
}
