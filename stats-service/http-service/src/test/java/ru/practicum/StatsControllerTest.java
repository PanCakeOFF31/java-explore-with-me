package ru.practicum;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.model.stat.dto.StatRequestDto;
import ru.practicum.model.stat.dto.StatResponseDto;
import ru.practicum.stat.StatsController;
import ru.practicum.stat.StatsService;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = StatsController.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class StatsControllerTest {
    private final MockMvc mvc;
    private final ObjectMapper mapper;
    @MockBean
    private StatsService statsService;
    @Captor
    private ArgumentCaptor<StatRequestDto> userArgumentCaptor;
    private StatRequestDto statRequestDto;
    private StatResponseDto statResponseDto1;
    private StatResponseDto statResponseDto2;
    private long responseHits1 = 11;
    private long responseHits2 = 11;
    private LocalDateTime timestamp;
    private String start = "2020-05-05 00:00:00";
    private String end = "2035-05-05 00:00:00";
    private String[] emptyUris = {};
    private String[] uris = {"/events", "/events/1"};

    @BeforeEach
    public void preTestInitialization() {
        timestamp = LocalDateTime.of(2022, 9, 6, 11, 0, 23);

        statRequestDto = StatRequestDto.builder()
                .app("ewm-main-service")
                .uri("/events/1")
                .ip("192.168.0.1")
                .timestamp(timestamp)
                .build();

        statResponseDto1 = StatResponseDto.builder()
                .app("ewm-main-service")
                .uri("/events")
                .hits(responseHits1)
                .build();

        statResponseDto2 = StatResponseDto.builder()
                .app("ewm-main-service")
                .uri("/events/3")
                .hits(responseHits2)
                .build();
    }

    @Test
    public void test_T0010_PS01_toHit() throws Exception {
        Mockito.doNothing()
                .when(statsService).toHit(statRequestDto);

        mvc.perform(post("/hit")
                        .content(mapper.writeValueAsString(statRequestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        System.out.println(mapper.writeValueAsString(statRequestDto));

        Mockito.verify(statsService, Mockito.only()).toHit(statRequestDto);
        Mockito.verifyNoMoreInteractions(statsService);
    }

    @ParameterizedTest
    @MethodSource("giveArgsFor_T0010_NS01")
    public void test_T0010_NS01_toHit_invalidContent(String content, String handler) throws Exception {
        String response = mvc.perform(post("/hit")
                        .content(content)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();
        System.out.println(response);
        assertTrue(response.contains(handler));
    }

    private static Stream<Arguments> giveArgsFor_T0010_NS01() {
        // NotEmpty/NotNull check
        return Stream.of(arguments("", "Required request body is missing"),
                arguments("{}", "Validation failed for argument"),
                arguments("{\"app\":\"ewm-main-service\",\"ip\":\"192.168.0.1\",\"timestamp\":\"2022-09-06 11:00:23\"}", "Validation failed for argument"),
                arguments("{\"app\":\"ewm-main-service\",\"uri\":\"/events/1\",\"timestamp\":\"2022-09-06 11:00:23\"}", "Validation failed for argument"),
                arguments("{\"app\":\"ewm-main-service\",\"uri\":\"/events/1\",\"ip\":\"192.168.0.1\"}", "Validation failed for argument"),
                arguments("{\"uri\":\"/events/1\",\"ip\":\"192.168.0.1\"}", "Validation failed for argument"),
                // Pattern check
                arguments("{\"app\":\"ewm-main-service\",\"uri\":\"/events/1\",\"ip\":\"192.168.0.0901\",\"timestamp\":\"2022-09-06 11:00:23\"}", "Validation failed for argument"));
    }

    @Test
    public void test_T0020_PS01_getStats() throws Exception {
        Mockito.when(statsService.getStats(anyString(), anyString(), any(String[].class), anyBoolean()))
                .thenReturn(List.of(statResponseDto1, statResponseDto2));

        mvc.perform(get("/stats")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .param("start", start)
                        .param("end", end)
                        .param("uris", Arrays.toString(emptyUris))
                        .param("unique", String.valueOf(false)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2));

        Mockito.verify(statsService, Mockito.only()).getStats(anyString(), anyString(), any(String[].class), anyBoolean());
        Mockito.verifyNoMoreInteractions(statsService);
    }

    @Test
    public void test_T0020_PS01_getStats_defaultParams() throws Exception {
        Mockito.when(statsService.getStats(anyString(), anyString(), any(String[].class), anyBoolean()))
                .thenReturn(List.of(statResponseDto1, statResponseDto2));

        mvc.perform(get("/stats")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .param("start", start)
                        .param("end", end))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2));

        Mockito.verify(statsService, Mockito.only()).getStats(anyString(), anyString(), any(String[].class), anyBoolean());
        Mockito.verifyNoMoreInteractions(statsService);
    }
}



















