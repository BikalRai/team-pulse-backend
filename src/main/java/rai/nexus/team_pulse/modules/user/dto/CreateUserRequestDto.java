package rai.nexus.team_pulse.modules.user.dto;

import rai.nexus.team_pulse.modules.user.enums.Role;

public record CreateUserRequestDto(
        String email,
        String password,
        Role role
) {
}
