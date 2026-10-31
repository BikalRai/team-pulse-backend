package rai.nexus.team_pulse.common.config.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;

import java.time.Instant;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
public class ApiResponse<T> {
    private final boolean success;
    private final int status;
    private final String message;
    private final T data;
    private final List<ErrorDetail> errors;
    private final PaginationMeta pagination;
    private final Instant timestamp;
    private final String path;
    private final String traceId;

    public ApiResponse(
            boolean success,
            int status,
            String message,
            T data,
            List<ErrorDetail> errors,
            PaginationMeta pagination,
            String path) {
        this.success = success;
        this.status = status;
        this.message = message;
        this.data = data;
        this.errors = errors;
        this.pagination = pagination;
        this.timestamp = Instant.now();
        this.path = path;
        this.traceId = MDC.get("traceId");
    }

    public static <T> ApiResponse<T> success(
            HttpStatus status,
            String message,
            T data,
            HttpServletRequest request
    ) {
        return new ApiResponse<>(
                true,
                status.value(),
                message,
                data,
                null,
                null,
                request.getRequestURI()
        );
    };

    public static <T> ApiResponse<T> successPaginated(
            HttpStatus status,
            String message,
            T data,
            PaginationMeta pagination,
            HttpServletRequest request
    ) {
        return new ApiResponse<>(
                true,
                status.value(),
                message,
                data,
                null,
                pagination,
                request.getRequestURI()
        );
    };

    public static <T> ApiResponse<T> error(
            int status,
            String message,
            List<ErrorDetail> errors,
            HttpServletRequest request
    ) {
        return new ApiResponse<>(
                false,
                status,
                message,
                null,
                errors,
                null,
                request.getRequestURI()
        );
    };
}
