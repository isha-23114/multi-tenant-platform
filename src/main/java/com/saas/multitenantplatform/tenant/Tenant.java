package com.saas.multitenantplatform.tenant;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tenants")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Tenant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tenant_key", unique = true, nullable = false)
    private String tenantKey;

    private String name;
    private String plan;
    private Boolean active = true;
}
