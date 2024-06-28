package ru.practicum;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class ExploreWithMeTest {
    @Test
    void test_T0010_PS01_startExploreWithMe() {
        assertDoesNotThrow(ExploreWithMe::new);
        assertDoesNotThrow(() -> ExploreWithMe.main(new String[]{}));
    }
}