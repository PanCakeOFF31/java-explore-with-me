package ru.practicum;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.model.Stat;
import ru.practicum.model.StatResponseViewDto;

import java.time.LocalDateTime;
import java.util.List;

public interface StatRepository extends JpaRepository<Stat, Long> {

    // uris - пустой и ip - уникальные
    @Query(value = "select a.app, ss.uri, count(ss.uri) as hits  " +
            "from (select distinct on (s.ip, s.app_id) * from stat as s) as ss " +
            "left join app as a on ss.app_id = a.id " +
            "where ss.requested between ?1 and ?2 " +
            "group by ss.uri, ss.app_id, a.app " +
            "order by hits desc ",
            nativeQuery = true)
    List<StatResponseViewDto> findAllUniqueByRequestedBetween(LocalDateTime start, LocalDateTime end);

    // uris - пустой и ip - неуникальные
    @Query(value = "select a.app, s.uri, count(s.uri) as hits " +
            "from stat as s " +
            "left join app as a on s.app_id = a.id " +
            "where s.requested between ?1 and ?2 " +
            "group by s.uri, s.app_id, a.app " +
            "order by hits desc ",
            nativeQuery = true)
    List<StatResponseViewDto> findAllByRequestedBetweenOrderByRequestedDesc(LocalDateTime start, LocalDateTime end);

    // uris - не пустой и ip - уникальные
    @Query(value = "select a.app, ss.uri, count(ss.uri) as hits  " +
            "from (select distinct on (s.ip, s.app_id) * from stat as s) as ss " +
            "left join app as a on ss.app_id = a.id " +
            "where (ss.requested between ?1 and ?2) and (ss.uri in (?3)) " +
            "group by ss.uri, ss.app_id, a.app " +
            "order by hits desc ",
            nativeQuery = true)
    List<StatResponseViewDto> findAllUniqueByRequestedBetweenAndUriIn(LocalDateTime start, LocalDateTime end, String[] uris);

    // uris - не пустой и ip - неуникальные
    @Query(value = "select a.app, s.uri, count(s.uri) as hits " +
            "from stat as s " +
            "left join app as a on s.app_id = a.id " +
            "where (s.requested between ?1 and ?2) and (s.uri in (?3)) " +
            "group by s.uri, s.app_id, a.app " +
            "order by hits desc ",
            nativeQuery = true)
    List<StatResponseViewDto> findAllByRequestedBetweenAndUriInOrderByRequestedDesc(LocalDateTime start, LocalDateTime end, String[] uris);
}
