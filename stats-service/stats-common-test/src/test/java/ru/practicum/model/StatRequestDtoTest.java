package ru.practicum.model;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.test.context.ContextConfiguration;
import ru.practicum.model.stat.dto.StatRequestDto;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.practicum.model.stat.dto.StatMapper.dateTimeFormatter;

@JsonTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ContextConfiguration()
class StatRequestDtoTest {
    private final JacksonTester<StatRequestDto> json;

    @Test
    public void test_T0010_PS01_foolField() throws IOException {
        LocalDateTime ldt = LocalDateTime.of(2020, 10, 12, 15, 45, 30);
        StatRequestDto statRequestDto = StatRequestDto.of("app-app", "some/uri/1", "10.10.10.10", ldt);

        JsonContent<StatRequestDto> result = json.write(statRequestDto);

        assertThat(result).extractingJsonPathStringValue("$.app").isEqualTo(statRequestDto.getApp());
        assertThat(result).extractingJsonPathStringValue("$.uri").isEqualTo(statRequestDto.getUri());
        assertThat(result).extractingJsonPathStringValue("$.ip").isEqualTo(statRequestDto.getIp());
        assertThat(result).extractingJsonPathStringValue("$.timestamp").isEqualTo(dateTimeFormatter.format(statRequestDto.getTimestamp()));

        assertThat(result).isStrictlyEqualToJson("{\"app\":\"app-app\",\"uri\":\"some/uri/1\",\"ip\":\"10.10.10.10\",\"timestamp\":\"2020-10-12 15:45:30\"}");
    }

    @Test
    public void test_T0010_NS01_emptyField() throws IOException {
        StatRequestDto statRequestDto = StatRequestDto.of();
        JsonContent<StatRequestDto> result = json.write(statRequestDto);

        assertThat(result).extractingJsonPathStringValue("$.app").isNull();
        assertThat(result).extractingJsonPathStringValue("$.uri").isNull();
        assertThat(result).extractingJsonPathStringValue("$.ip").isNull();
        assertThat(result).extractingJsonPathStringValue("$.timestamp").isNull();

        assertThat(result).isStrictlyEqualToJson("{\"app\":null,\"uri\":null,\"ip\":null,\"timestamp\":null}");
    }
}