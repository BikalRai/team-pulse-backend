package rai.nexus.team_pulse.modules.user.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rai.nexus.team_pulse.common.config.response.ApiResponse;
import rai.nexus.team_pulse.common.config.response.PaginationMeta;
import rai.nexus.team_pulse.modules.user.dto.CreateUserRequestDto;
import rai.nexus.team_pulse.modules.user.dto.UpdateUserRequestDto;
import rai.nexus.team_pulse.modules.user.dto.UserResponseDto;
import rai.nexus.team_pulse.modules.user.service.UserService;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<ApiResponse<UserResponseDto>> inviteUser(
            @Valid @RequestBody CreateUserRequestDto dto,
            HttpServletRequest request
            ) {

        UserResponseDto result = userService.create(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.success(
                        HttpStatus.CREATED,
                        "User invited successfully",
                        result,
                        request
                )
        );
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<UserResponseDto>>> getUsers(
            @RequestParam(defaultValue = "1") @Min(1) int page,
            @RequestParam(name = "perPage", defaultValue = "20") @Min(1) @Max(100) int perPage,
            HttpServletRequest request
    ) {

        Pageable pageable = PageRequest.of(page - 1, perPage);

        Page<UserResponseDto> users = userService.getAll(pageable);

        PaginationMeta pagination = new PaginationMeta(
                users.getNumber() + 1, // returns the page (0 indexed),
                users.getSize(), // returns requested limit
                users.getTotalElements(), // returns the absolute total number of rows in the db
                users.getTotalPages() // returns the number of pages that exists
        );

        return ResponseEntity.ok(
                ApiResponse.successPaginated(
                        "Users retrieved successfully",
                        users.getContent(), // returns the list for the actual page requested
                        pagination,
                        request
                )
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponseDto>> getUserById(
            @PathVariable UUID id,
            HttpServletRequest request
            ) {

        UserResponseDto result = userService.getOne(id);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "User retrieved successfully",
                        result,
                        request
                )
        );
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponseDto>> updateUser(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateUserRequestDto dto,
            HttpServletRequest request
    ) {

        UserResponseDto result = userService.update(id, dto);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "User updated successfully",
                        result,
                        request
                )
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(
            @PathVariable UUID id,
            HttpServletRequest request
    ) {

        userService.delete(id);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "User deleted successfully",
                        null,
                        request
                )
        );
    }
}
