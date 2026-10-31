package rai.nexus.team_pulse.common.config.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ErrorDetail {
    private String field;
    private String message;
}
