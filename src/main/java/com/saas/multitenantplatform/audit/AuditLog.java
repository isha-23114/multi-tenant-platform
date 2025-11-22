package com.saas.multitenantplatform.audit;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "audit_logs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String tenantKey;
    private String entityName;
    private String entityId;
    private String action; // CREATE, UPDATE, DELETE
    private String changedByEmail;
    private java.time.Instant changedAt;
    @Column(columnDefinition = "text")
    private String details;
}
