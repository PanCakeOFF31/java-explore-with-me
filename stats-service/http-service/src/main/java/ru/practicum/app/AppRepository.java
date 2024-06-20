package ru.practicum.app;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.model.app.App;

import java.util.Optional;

public interface AppRepository extends JpaRepository<App, Long> {
    Optional<App> findByApp(String app);
}
