package ru.practicum.location.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.location.model.Location;

import java.util.Optional;

public interface LocationRepository extends JpaRepository<Location, Long> {
    boolean existsByLatAndLon(float lat, float lon);

    Optional<Location> findByLatAndLon(float lat, float lon);

    @Query(value = "select l.id, l.latitude, l.longitude from location as l " +
            "left join event as e on l.id = e.location_id " +
            "where l.id = :locationId " +
            "limit 1",
            nativeQuery = true)
    Optional<Location> findFirstUsedLocation(@Param("locationId") long locationId);
}
