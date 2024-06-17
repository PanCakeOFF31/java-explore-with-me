package ru.practicum;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.model.StatRequestDto;
import ru.practicum.model.StatResponseDto;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping
@RequiredArgsConstructor
public class StatsController {
    private final StatsService statsService;

    @PostMapping(path = "/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public void toHit(@Valid @RequestBody StatRequestDto requestDto) {
        log.debug("/hit - POST: toHit({})", requestDto);
        statsService.toHit(requestDto);
    }

    @GetMapping(path = "/stats")
    @ResponseStatus(HttpStatus.OK)
    public List<StatResponseDto> getStats(@RequestParam String start,
                                          @RequestParam String end,
                                          @RequestParam(required = false, defaultValue = "") String[] uris,
                                          @RequestParam(required = false, defaultValue = "false") boolean unique) {
        log.debug("/stats - GET: getStats({}, {}, {}, {})", start, end, uris, unique);

        start = URLDecoder.decode(start, StandardCharsets.UTF_8);
        end = URLDecoder.decode(end, StandardCharsets.UTF_8);

        return statsService.getStats(start, end, uris, unique);
    }
}
