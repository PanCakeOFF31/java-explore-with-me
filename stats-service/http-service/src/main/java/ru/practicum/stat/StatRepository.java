package ru.practicum.stat;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.model.stat.Stat;
import ru.practicum.model.stat.dto.StatResponseViewDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface StatRepository extends JpaRepository<Stat, Long> {

    // uris - пустой и ip - уникальные
    @Query(value = "select a.app, s.uri, count(distinct s.ip) as hits " +
            "from stat as s " +
            "left join app as a on s.app_id = a.id " +
            "where s.requested between ?1 and ?2 " +
            "group by s.uri, a.app " +
            "order by hits desc ",
            nativeQuery = true)
    List<StatResponseViewDto> findAllUniqueByRequestedBetween(LocalDateTime start, LocalDateTime end);

    // uris - пустой и ip - неуникальные
    @Query(value = "select a.app, s.uri, count(s.ip) as hits " +
            "from stat as s " +
            "left join app as a on s.app_id = a.id " +
            "where s.requested between ?1 and ?2 " +
            "group by s.uri, a.app " +
            "order by hits desc ",
            nativeQuery = true)
    List<StatResponseViewDto> findAllByRequestedBetweenOrderByRequestedDesc(LocalDateTime start, LocalDateTime end);

    // uris - не пустой и ip - уникальные
    @Query(value = "select a.app, s.uri, count(distinct s.ip) as hits " +
            "from stat as s " +
            "left join app as a on s.app_id = a.id " +
            "where (s.requested between ?1 and ?2) and (s.uri in (?3)) " +
            "group by s.uri, a.app " +
            "order by hits desc ",
            nativeQuery = true)
    List<StatResponseViewDto> findAllUniqueByRequestedBetweenAndUriIn(LocalDateTime start, LocalDateTime end, String[] uris);

    // uris - не пустой и ip - неуникальные
    @Query(value = "select a.app, s.uri, count(s.ip) as hits " +
            "from stat as s " +
            "left join app as a on s.app_id = a.id " +
            "where (s.requested between ?1 and ?2) and (s.uri in (?3)) " +
            "group by s.uri, a.app " +
            "order by hits desc ",
            nativeQuery = true)
    List<StatResponseViewDto> findAllByRequestedBetweenAndUriInOrderByRequestedDesc(LocalDateTime start, LocalDateTime end, String[] uris);

    @Query(value = "select count(distinct s.ip) from stat as s " +
            "where s.uri = ?1 " +
            "group by s.ip ", nativeQuery = true)
    Optional<Long> getUniqueEventViewsByUri(String eventUri);
}
