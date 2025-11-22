package com.saas.multitenantplatform.audit;

import com.saas.multitenantplatform.tenant.TenantContext;
import com.saas.multitenantplatform.user.User;
import com.saas.multitenantplatform.security.JwtUtil; // not necessary; we take current principal email via SecurityContext
import jakarta.persistence.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class AuditEntityListener {

    private static AuditLogRepository auditRepo;

    // Spring will inject repo via setter because listener is instantiated by JPA — we use static holder
    public static void setAuditLogRepository(AuditLogRepository repo) {
        auditRepo = repo;
    }

    private String currentUserEmail() {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.getPrincipal() instanceof org.springframework.security.core.userdetails.UserDetails) {
                return ((org.springframework.security.core.userdetails.UserDetails) auth.getPrincipal()).getUsername();
            }
        } catch (Exception ignored) {}
        return null;
    }

    @PrePersist
    public void onCreate(Object entity) {
        record(entity, "CREATE", null);
    }

    @PreUpdate
    public void onUpdate(Object entity) {
        record(entity, "UPDATE", null);
    }

    @PreRemove
    public void onDelete(Object entity) {
        record(entity, "DELETE", null);
    }

    private void record(Object entity, String action, String details) {
        if (auditRepo == null) return; // repository not yet wired
        String tenant = TenantContext.getTenantId();
        String entityName = entity.getClass().getSimpleName();
        String entityId = extractId(entity);
        AuditLog al = AuditLog.builder()
                .tenantKey(tenant)
                .entityName(entityName)
                .entityId(entityId)
                .action(action)
                .changedByEmail(currentUserEmail())
                .changedAt(Instant.now())
                .details(details)
                .build();
        auditRepo.save(al);
    }

    private String extractId(Object entity) {
        try {
            java.lang.reflect.Field idf = entity.getClass().getDeclaredField("id");
            idf.setAccessible(true);
            Object id = idf.get(entity);
            return id == null ? "null" : String.valueOf(id);
        } catch (Exception e) {
            return "unknown";
        }
    }
}
