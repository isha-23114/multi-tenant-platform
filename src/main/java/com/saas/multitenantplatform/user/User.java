package com.saas.multitenantplatform.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.saas.multitenantplatform.tenant.TenantAware;
import com.saas.multitenantplatform.tenant.TenantEntityListener;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@EntityListeners({TenantEntityListener.class, com.saas.multitenantplatform.audit.AuditEntityListener.class})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User extends TenantAware {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    private String fullName;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String passwordHash;

    // role stored as string; default TENANT_USER
    @Column(nullable = false)
    private String role = "TENANT_USER";
}
