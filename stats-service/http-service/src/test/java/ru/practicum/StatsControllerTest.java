package ru.practicum;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = StatsController.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class StatsControllerTest {
    private final MockMvc mvc;
    private final ObjectMapper mapper;
    @MockBean
    private StatsService statsService;


    @BeforeEach
    public void preTestInitialization() {

    }

    @Test
    public void test_T0010_PS01_toHit() {

    }

    @Test
    public void test_T0010_NS01_toHit_invalidContent() {

    }
}