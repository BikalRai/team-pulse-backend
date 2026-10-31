package rai.nexus.team_pulse.common.config.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import rai.nexus.team_pulse.common.config.response.ApiResponse;
import rai.nexus.team_pulse.common.config.response.ErrorDetail;

import java.util.List;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiResponse<Void>> handleBadRequest(
            BadRequestException ex,
            HttpServletRequest request
    ) {
        log.warn("Bad request on {} {}: {}",
                request.getMethod(), request.getRequestURI(), ex.getMessage()
        );

        return buildError(
                HttpStatus.BAD_REQUEST,
                ex.getMessage(),
                null,
                request
        );
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleResourceNotFound(
            ResourceNotFoundException ex,
            HttpServletRequest request
    ) {

        log.warn("Resource not found on {} {}: {}",
                request.getMethod(), request.getRequestURI(), ex.getMessage()
        );

        return buildError(
                HttpStatus.NOT_FOUND,
                ex.getMessage(),
                null,
                request
        );
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ApiResponse<Void>> handleConflict(
            ConflictException ex,
            HttpServletRequest request
    ) {

        log.warn("Data conflict on {} {}: {}",
                request.getMethod(), request.getRequestURI(), ex.getMessage()
        );

        return buildError(
                HttpStatus.CONFLICT,
                ex.getMessage(),
                null,
                request
        );
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ApiResponse<Void>> handleUnauthorized(
            UnauthorizedException ex,
            HttpServletRequest request
    ) {

        log.warn("User unauthorized for {} {}: {}",
                request.getMethod(), request.getRequestURI(), ex.getMessage()
        );

        return buildError(
                HttpStatus.UNAUTHORIZED,
                ex.getMessage(),
                null,
                request
        );
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<Void>> handleAccessDenied(
            AccessDeniedException ex,
            HttpServletRequest request
    ) {

        log.warn("Access denied on {} {}: {}",
                request.getMethod(), request.getRequestURI(), ex.getMessage()
        );

        return buildError(
                HttpStatus.FORBIDDEN,
                ex.getMessage(),
                null,
                request
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpServletRequest request
    ) {

        List<ErrorDetail> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(err -> new ErrorDetail(
                        err.getField(), err.isBindingFailure() ?
                        "Invalid value" :
                        err.getDefaultMessage()
                )).toList();


        log.warn("Method argument not valid on {} {}: {}",
                request.getMethod(), request.getRequestURI(), ex.getMessage()
        );

        return buildError(
                HttpStatus.BAD_REQUEST,
                "Validation failed",
                errors,
                request
        );
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiResponse<Void>> handleMissingParameter(
            MissingServletRequestParameterException ex,
            HttpServletRequest request
    ) {

        log.warn("Request missing parameters on {} {}: {}",
                request.getMethod(), request.getRequestURI(), ex.getMessage()
        );

        return buildError(
                HttpStatus.BAD_REQUEST,
                "Missing requried parameter: " + ex.getMessage(),
                null,
                request
        );
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse<Void>> handleMethodArgumentTypeMismatch(
            MethodArgumentTypeMismatchException ex,
            HttpServletRequest request
    ) {

        log.warn("Method argues type mismatch on {} {}: {}",
                request.getMethod(), request.getRequestURI(), ex.getMessage()
        );

        return buildError(
                HttpStatus.BAD_REQUEST,
                "Parameter: " + ex.getName() + "has an invalid value",
                null,
                request
        );
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiResponse<Void>> handleBadCredentials(
            BadCredentialsException ex,
            HttpServletRequest request
    ) {

        log.warn("User bad credentials on {} {}: {}",
                request.getMethod(), request.getRequestURI(), ex.getMessage()
        );

        return buildError(
                HttpStatus.UNAUTHORIZED,
                "Invalid credentials",
                null,
                request
        );
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<Void>> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex,
            HttpServletRequest request
    ) {
        log.warn("Malformed JSON request received: {} {}: {}",
                request.getMethod(), request.getRequestURI(), ex.getMessage()
        );

        return buildError(
                HttpStatus.BAD_REQUEST,
                "Malformed JSON request or invalid data types provided",
                null,
                request
        );
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiResponse<Void>> handleHttpRequestMethodNotSupported(
            HttpRequestMethodNotSupportedException ex,
            HttpServletRequest request
    ) {
        log.warn("Unsupported HTTP method {} on {} ",
                request.getMethod(), request.getRequestURI()
        );

        return buildError(
                HttpStatus.METHOD_NOT_ALLOWED,
                "HTTP method " + ex.getMethod() + " is not supported for this endpoint",
                null,
                request
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleException(
            Exception ex,
            HttpServletRequest request
    ) {

        log.error("Unexpected error occurred on {} {}", request.getMethod(), request.getRequestURI(), ex);

        return buildError(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "An unexpected error occurred: ",
                null,
                request
        );
    }

    private ResponseEntity<ApiResponse<Void>> buildError(
            HttpStatus status,
            String message,
            List<ErrorDetail> errors,
            HttpServletRequest request
    ) {
        ApiResponse<Void> response = ApiResponse.error(
                status.value(),
                message,
                errors,
                request
        );
        return ResponseEntity.status(status).body(response);
    }
}
