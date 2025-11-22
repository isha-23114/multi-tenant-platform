package com.saas.multitenantplatform.audit;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
    List<AuditLog> findTop20ByTenantKeyOrderByChangedAtDesc(String tenantKey);
}
