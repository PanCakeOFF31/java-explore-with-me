package ru.practicum;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class StatsServiceTest {
    @InjectMocks
    private StatsServiceImpl statsService;
    @Mock
    private AppRepository appRepository;
    @Mock
    private StatRepository statRepository;

    @BeforeEach
    public void preTestInitialization() {
    }

    @Test
    public void test_T0010_PS01_toHit() {
    }
}
