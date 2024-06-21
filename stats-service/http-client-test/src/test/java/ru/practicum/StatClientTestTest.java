package ru.practicum;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.client.MockRestServiceServer;
import ru.practicum.model.stat.dto.StatMapper;
import ru.practicum.model.stat.dto.StatRequestDto;
import ru.practicum.model.stat.dto.StatResponseDto;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

@SpringBootTest
@AutoConfigureWebClient
class StatClientTestTest {
    private static final String POSTMAN_MOCK_SERVER_URL = "https://6e83aceb-7baf-4757-a0f1-d9510f26e822.mock.pstmn.io";
    @Value("${stats-server.url}")
    private String realUrl;
    private String mockServerUrl = "http://localhost:9090";
    private StatClientImpl statClient;
    @Autowired
    private RestTemplateBuilder builder;
    @Autowired
    private ObjectMapper mapper;

    //    Использовать при активном Postman Server
    @Nested
    class UnorderedMockServerTest {
        @BeforeEach
        public void initialize() {
            statClient = new StatClientImpl(POSTMAN_MOCK_SERVER_URL, builder);
        }

        @Test
        @Disabled
        public void test_T0010_PS01_toHit() {
            LocalDateTime ldt = LocalDateTime.of(2025, 5, 5, 0, 0, 0);
            StatRequestDto statRequestDto = StatRequestDto.of("app", "/events/10", "10.10.10.15", ldt);
            ResponseEntity<Object> response = statClient.toHit(statRequestDto);
            assertTrue(response.getStatusCode().is2xxSuccessful());
            assertFalse(response.hasBody());
        }

        @Test
        @Disabled
        public void test_T0011_PS01_getStats_noUris_And_notUnique() {
            LocalDateTime startLDT = LocalDateTime.of(2020, 5, 5, 0, 0, 0);
            LocalDateTime endLDT = LocalDateTime.of(2035, 5, 5, 0, 0, 0);
            String start = StatMapper.dateTimeFormatter.format(startLDT);
            String end = StatMapper.dateTimeFormatter.format(endLDT);
            String[] uris = new String[0];
            boolean unique = false;

            ResponseEntity<Object> response = statClient.getStats(start, end, uris, unique);
            assertTrue(response.getStatusCode().is2xxSuccessful());

            List<Object> stats = mapper.convertValue(response.getBody(), List.class);
            assertNotNull(stats);
            assertEquals(2, stats.size());

            StatResponseDto stat1 = mapper.convertValue(stats.get(0), StatResponseDto.class);
            assertNotNull(stat1);
            assertEquals("ewm-main-service", stat1.getApp());
            assertEquals("/events", stat1.getUri());
            assertEquals(1, stat1.getHits());

            StatResponseDto stat2 = mapper.convertValue(stats.get(1), StatResponseDto.class);
            assertNotNull(stat2);
            assertEquals("ewm-main-service", stat2.getApp());
            assertEquals("/events/5", stat2.getUri());
            assertEquals(1, stat2.getHits());
        }
    }

    //    Использовать при запущенном сервере
    @Nested
    @TestMethodOrder(MethodOrderer.MethodName.class)
    class OrderedIntegrationTestWithoutRollbackWithRealActiveServer {
        @BeforeEach
        public void initialize() {
            statClient = new StatClientImpl(realUrl, builder);
        }

        @Test
        @Disabled
        public void test_T1010_PS01_toHit() {
            LocalDateTime ldt = LocalDateTime.of(2025, 5, 5, 0, 0, 0);
            StatRequestDto statRequestDto = StatRequestDto.of("app", "/events/10", "10.10.10.15", ldt);
            ResponseEntity<Object> response = statClient.toHit(statRequestDto);
            assertTrue(response.getStatusCode().is2xxSuccessful());
        }

        @Test
        @Disabled
        public void test_T1020_PS01_getStats() {
            LocalDateTime startLDT = LocalDateTime.of(2020, 5, 5, 0, 0, 0);
            LocalDateTime endLDT = LocalDateTime.of(2035, 5, 5, 0, 0, 0);
            String start = StatMapper.dateTimeFormatter.format(startLDT);
            String end = StatMapper.dateTimeFormatter.format(endLDT);
            String[] uris = new String[0];
            boolean unique = false;

            ResponseEntity<Object> response = statClient.getStats(start, end, uris, unique);
            assertTrue(response.getStatusCode().is2xxSuccessful());

            List<Object> stats = mapper.convertValue(response.getBody(), List.class);
            assertNotNull(stats);
            assertEquals(1, stats.size());

            StatResponseDto stat1 = mapper.convertValue(stats.get(0), StatResponseDto.class);
            assertNotNull(stat1);
            assertEquals("app", stat1.getApp());
            assertEquals("/events/10", stat1.getUri());
            assertEquals(1, stat1.getHits());
        }

        @Test
        @Disabled
        public void test_T1030_PS01_toHit() {
            LocalDateTime ldt = LocalDateTime.of(2025, 5, 6, 0, 0, 0);
            StatRequestDto statRequestDto = StatRequestDto.of("app", "/events/15", "10.10.10.15", ldt);
            ResponseEntity<Object> response = statClient.toHit(statRequestDto);
            assertTrue(response.getStatusCode().is2xxSuccessful());
            response = statClient.toHit(statRequestDto);
            assertTrue(response.getStatusCode().is2xxSuccessful());
        }

        @Test
        @Disabled
        public void test_T1040_PS01_getStats() {
            LocalDateTime startLDT = LocalDateTime.of(2020, 5, 5, 0, 0, 0);
            LocalDateTime endLDT = LocalDateTime.of(2035, 5, 5, 0, 0, 0);
            String start = StatMapper.dateTimeFormatter.format(startLDT);
            String end = StatMapper.dateTimeFormatter.format(endLDT);
            String[] uris = new String[0];
            boolean unique = false;

            ResponseEntity<Object> response = statClient.getStats(start, end, uris, unique);
            assertTrue(response.getStatusCode().is2xxSuccessful());

            List<Object> stats = mapper.convertValue(response.getBody(), List.class);
            assertNotNull(stats);
            assertEquals(2, stats.size());

            StatResponseDto stat1 = mapper.convertValue(stats.get(0), StatResponseDto.class);
            assertNotNull(stat1);
            assertEquals("app", stat1.getApp());
            assertEquals("/events/15", stat1.getUri());
            assertEquals(2, stat1.getHits());
        }
    }

    @Nested
    class MockResServer {
        HttpHeaders headers = new HttpHeaders();
        MockRestServiceServer mockServer;

        @BeforeEach
        public void initialize() {
            statClient = new StatClientImpl(mockServerUrl, builder);
            mockServer = MockRestServiceServer.createServer(statClient.getRest());

            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        }

        @Test
        public void test_T2010_PS01_toHit() throws JsonProcessingException {
            LocalDateTime ldt = LocalDateTime.of(2025, 5, 5, 0, 0, 0);
            StatRequestDto statRequestDto = StatRequestDto.of("app", "/events/10", "10.10.10.15", ldt);

            mockServer.expect(requestTo(mockServerUrl + "/hit"))
                    .andExpect(content().json(mapper.writeValueAsString(statRequestDto)))
                    .andRespond(withStatus(HttpStatus.OK)
                            .headers(headers)
                            .body(""));

            ResponseEntity<Object> response = statClient.toHit(statRequestDto);
            assertTrue(response.getStatusCode().is2xxSuccessful());
            assertFalse(response.hasBody());

            mockServer.verify();
        }

        @Test
        public void test_T2020_PS01_getStats_fullArguments() throws JsonProcessingException {
            LocalDateTime startLDT = LocalDateTime.of(2020, 5, 5, 0, 0, 0);
            LocalDateTime endLDT = LocalDateTime.of(2035, 5, 5, 0, 0, 0);
            String start = StatMapper.dateTimeFormatter.format(startLDT);
            String end = StatMapper.dateTimeFormatter.format(endLDT);
            String[] uris = new String[0];
            boolean unique = false;

            StatResponseDto responseDto = StatResponseDto.of("app", "/events/11", 13);

            mockServer.expect(requestTo(mockServerUrl + "/stats?start=2020-05-05%2000%3A00%3A00&end=2035-05-05%2000%3A00%3A00&uris=&unique=false"))
                    .andExpect(queryParam("start", "2020-05-05%2000%3A00%3A00"))
                    .andExpect(queryParam("end", "2035-05-05%2000%3A00%3A00"))
                    .andExpect(queryParam("uris", ""))
                    .andExpect(queryParam("unique", String.valueOf(unique)))
                    .andRespond(withStatus(HttpStatus.OK)
                            .headers(headers)
                            .body(List.of(mapper.writeValueAsString(responseDto)).toString()));

            ResponseEntity<Object> response = statClient.getStats(start, end, uris, unique);
            assertTrue(response.getStatusCode().is2xxSuccessful());
            assertEquals(responseDto, mapper.convertValue(mapper.convertValue(response.getBody(), List.class).get(0), StatResponseDto.class));

            mockServer.verify();
        }

        @Test
        public void test_T2030_PS01_getStats_twoArguments() throws JsonProcessingException {
            LocalDateTime startLDT = LocalDateTime.of(2020, 5, 5, 0, 0, 0);
            LocalDateTime endLDT = LocalDateTime.of(2035, 5, 5, 0, 0, 0);
            String start = StatMapper.dateTimeFormatter.format(startLDT);
            String end = StatMapper.dateTimeFormatter.format(endLDT);

            StatResponseDto responseDto = StatResponseDto.of("app", "/events/11", 13);

            mockServer.expect(requestTo(mockServerUrl + "/stats?start=2020-05-05%2000%3A00%3A00&end=2035-05-05%2000%3A00%3A00&uris=&unique=false"))
                    .andExpect(queryParam("start", "2020-05-05%2000%3A00%3A00"))
                    .andExpect(queryParam("end", "2035-05-05%2000%3A00%3A00"))
                    .andExpect(queryParam("uris", ""))
                    .andExpect(queryParam("unique", String.valueOf(false)))
                    .andRespond(withStatus(HttpStatus.OK)
                            .headers(headers)
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(mapper.writeValueAsString(responseDto)));

            ResponseEntity<Object> response = statClient.getStats(start, end);
            assertTrue(response.getStatusCode().is2xxSuccessful());
            assertEquals(responseDto, mapper.convertValue(response.getBody(), StatResponseDto.class));

            mockServer.verify();
        }

        @Test
        public void test_T2040_PS01_getStats_fullArguments() throws JsonProcessingException {
            LocalDateTime startLDT = LocalDateTime.of(2020, 5, 5, 0, 0, 0);
            LocalDateTime endLDT = LocalDateTime.of(2035, 5, 5, 0, 0, 0);
            String start = StatMapper.dateTimeFormatter.format(startLDT);
            String end = StatMapper.dateTimeFormatter.format(endLDT);
            String[] uris = {"/events", "/events/11"};

            StatResponseDto responseDto = StatResponseDto.of("app", "/events/11", 13);

            mockServer.expect(requestTo(mockServerUrl + "/stats?start=2020-05-05%2000%3A00%3A00&end=2035-05-05%2000%3A00%3A00&uris=%2Fevents%2C%2Fevents%2F11&unique=false"))
                    .andExpect(queryParam("start", "2020-05-05%2000%3A00%3A00"))
                    .andExpect(queryParam("end", "2035-05-05%2000%3A00%3A00"))
                    .andExpect(queryParam("uris", "%2Fevents%2C%2Fevents%2F11"))
                    .andExpect(queryParam("unique", String.valueOf(false)))
                    .andRespond(withStatus(HttpStatus.OK)
                            .headers(headers)
                            .body(List.of(mapper.writeValueAsString(responseDto)).toString()));

            ResponseEntity<Object> response = statClient.getStats(start, end, uris);
            assertTrue(response.getStatusCode().is2xxSuccessful());
            assertEquals(responseDto, mapper.convertValue(mapper.convertValue(response.getBody(), List.class).get(0), StatResponseDto.class));

            mockServer.verify();
        }
    }
}