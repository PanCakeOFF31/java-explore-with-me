package ru.practicum.model;


import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class StatResponseDtoTest {
    private final JacksonTester<StatResponseDto> json;

    @Test
    public void test_T0010_PS01_foolField() throws IOException {
        StatResponseDto statRequestDto = StatResponseDto.of("app-app", "some/uri/1", 15L);

        JsonContent<StatResponseDto> result = json.write(statRequestDto);

        assertThat(result).extractingJsonPathStringValue("$.app").isEqualTo(statRequestDto.getApp());
        assertThat(result).extractingJsonPathStringValue("$.uri").isEqualTo(statRequestDto.getUri());
        assertThat(result).extractingJsonPathNumberValue("$.hits");

        assertThat(result).isStrictlyEqualToJson("{\"app\":\"app-app\",\"uri\":\"some/uri/1\",\"hits\":15}");
    }

    @Test
    public void test_T0010_NS01_emptyField() throws IOException {
        StatResponseDto statRequestDto = StatResponseDto.of();
        JsonContent<StatResponseDto> result = json.write(statRequestDto);

        assertThat(result).extractingJsonPathStringValue("$.app").isNull();
        assertThat(result).extractingJsonPathStringValue("$.uri").isNull();
        assertThat(result).extractingJsonPathNumberValue("$.hits");
        assertThat(result).isStrictlyEqualToJson("{\"app\":null,\"uri\":null,\"hits\":0}");
    }
}