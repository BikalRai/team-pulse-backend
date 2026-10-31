package rai.nexus.team_pulse;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import rai.nexus.team_pulse.common.config.response.ApiResponse;

@RestController
public class TestController {
    @GetMapping
    public ResponseEntity<ApiResponse<Void>> test(
            HttpServletRequest request
    ) {
        return ResponseEntity.ok(ApiResponse.success(
                HttpStatus.OK,
                "Hello World",
                null,
                request
        ));
    }
}
