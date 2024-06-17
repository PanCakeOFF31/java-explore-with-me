package ru.practicum.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor(staticName = "of")
@AllArgsConstructor(staticName = "of")
@Builder(toBuilder = true)
public class StatRequestDto {
    @NotEmpty
    private String app;
    @NotEmpty
    private String uri;
    @NotEmpty
    @Pattern(regexp = "^[0-9]{1,3}.[0-9]{1,3}.[0-9]{1,3}.[0-9]{1,3}$", message = "IP-адрес не соответствует стандарту")
    private String ip;
    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;
}