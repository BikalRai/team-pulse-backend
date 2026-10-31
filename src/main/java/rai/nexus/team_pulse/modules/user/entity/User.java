package rai.nexus.team_pulse.modules.user.entity;

import jakarta.persistence.*;
import lombok.*;
import rai.nexus.team_pulse.common.entity.BaseEntity;
import rai.nexus.team_pulse.modules.user.enums.Role;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "users")
public class User extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Builder.Default
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "tenant_id", nullable = true)
//    private Tenant tenantId;

//    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
//    @JoinColumn(name = "tenant_role_id", nullable = true)
//    private TenantRole tenantRole;
}
