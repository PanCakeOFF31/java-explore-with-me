package ru.practicum;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class StatsServerTest {
    @Test
    void test_T0010_PS01_startMain() {
        assertDoesNotThrow(StatsServer::new);
        assertDoesNotThrow(() -> StatsServer.main(new String[]{}));
    }
}