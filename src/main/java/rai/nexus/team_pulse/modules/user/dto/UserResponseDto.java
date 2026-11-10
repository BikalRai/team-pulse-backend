package rai.nexus.team_pulse.modules.user.dto;

import rai.nexus.team_pulse.modules.user.enums.Role;

import java.time.Instant;

public record UserResponseDto(
        String id,
        String email,
        Role role,
        Boolean isActive,
        Instant createdAt,
        Instant updatedAt,
        Long version
) {
}
