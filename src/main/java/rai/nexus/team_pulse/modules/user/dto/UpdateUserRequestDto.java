package rai.nexus.team_pulse.modules.user.dto;

import rai.nexus.team_pulse.modules.user.enums.Role;

public record UpdateUserRequestDto(
        String email,
        Role role
) {
}
