package com.saas.multitenantplatform.audit;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.boot.context.event.ApplicationReadyEvent;

@Component
public class AuditConfig {

    private final AuditLogRepository repo;

    public AuditConfig(AuditLogRepository repo) {
        this.repo = repo;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void setUp() {
        AuditEntityListener.setAuditLogRepository(repo);
    }
}
