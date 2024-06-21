package ru.practicum;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.model.stat.dto.StatRequestDto;

import java.util.List;
import java.util.Map;

@Slf4j
@Getter
@Service
public class StatClientImpl implements StatClient {
    private final RestTemplate rest;
    private static final String API_PREFIX = "";

    @Autowired
    public StatClientImpl(@Value("${stats-server.url}") String serverUrl, RestTemplateBuilder builder) {
        rest = builder
                .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .build();

        log.debug("StatClient(URL={}{})", serverUrl, API_PREFIX);
    }

    public ResponseEntity<Object> toHit(StatRequestDto statRequestDto) {
        log.debug("StatClientImpl - statClient.createItem({})", statRequestDto);
        return post("/hit", statRequestDto);
    }

    private <T> ResponseEntity<Object> post(String path, T body) {
        return makeAndSendRequest(HttpMethod.POST, path, null, body);
    }

    public ResponseEntity<Object> getStats(String start,
                                           String end) {
        log.debug("StatClientImpl - statClient.getStats(start={}, end={})", start, end);
        return getStats(start, end, new String[0], false);
    }

    public ResponseEntity<Object> getStats(String start,
                                           String end,
                                           String[] uris) {
        log.debug("StatClientImpl - statClient.getStats(start={}, end={}, uris={})", start, end, uris);
        return getStats(start, end, uris, false);
    }

    public ResponseEntity<Object> getStats(String start,
                                           String end,
                                           boolean unique) {
        log.debug("StatClientImpl - statClient.getStats(start={}, end={}, unique={})", start, end, unique);
        return getStats(start, end, new String[0], unique);
    }

    public ResponseEntity<Object> getStats(String start,
                                           String end,
                                           String[] uris,
                                           boolean unique) {
        log.debug("StatClientImpl - statClient.getStats({}, {}, {}, {})", start, end, uris, unique);
        Map<String, Object> parameters = Map.of("start", start, "end", end, "uris", uris, "unique", unique);
        return get("/stats" + "?start={start}&end={end}&uris={uris}&unique={unique}", parameters);
    }

    private ResponseEntity<Object> get(String path, @Nullable Map<String, Object> parameters) {
        log.debug("StatClientImpl - statClient.get({}, {})", path, parameters);
        return makeAndSendRequest(HttpMethod.GET, path, parameters, null);
    }

    @Override
    public long getUniqueEventViews(long eventId) {
        log.debug("StatClientImpl - statClient.getUniqueEventViews(eventId={})", eventId);
        Object responseBody = get("/stats/" + eventId, null).getBody();
        System.out.println(responseBody);

        try {
            return Long.valueOf(responseBody.toString());
        } catch (RuntimeException ignore) {
            log.warn("Проблемы с возвращаемым телом от StatsService");
            return 0;
        }
    }

    private <T> ResponseEntity<Object> makeAndSendRequest(@NonNull HttpMethod method,
                                                          @NonNull String path,
                                                          @Nullable Map<String, Object> requestParameters,
                                                          @Nullable T body) {
        HttpEntity<T> requestEntity = new HttpEntity<>(body, defaultHeaders());

        ResponseEntity<Object> statsServerResponse;
        try {
            if (requestParameters != null) {
                statsServerResponse = rest.exchange(path, method, requestEntity, Object.class, requestParameters);
            } else {
                statsServerResponse = rest.exchange(path, method, requestEntity, Object.class);
            }
        } catch (HttpStatusCodeException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsByteArray());
        }

        return prepareGatewayResponse(statsServerResponse);
    }

    private HttpHeaders defaultHeaders() {
        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        return headers;
    }

    private ResponseEntity<Object> prepareGatewayResponse(ResponseEntity<Object> response) {
        if (response.getStatusCode().is2xxSuccessful()) {
            return response;
        }

        ResponseEntity.BodyBuilder responseBuilder = ResponseEntity.status(response.getStatusCode());

        if (response.hasBody()) {
            return responseBuilder.body(response.getBody());
        }

        return responseBuilder.build();
    }
}
