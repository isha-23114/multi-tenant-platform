package com.saas.multitenantplatform.project;

import com.saas.multitenantplatform.tenant.TenantAware;
import com.saas.multitenantplatform.tenant.TenantEntityListener;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "projects")
@EntityListeners({TenantEntityListener.class, com.saas.multitenantplatform.audit.AuditEntityListener.class})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Project extends TenantAware {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String description;
}
