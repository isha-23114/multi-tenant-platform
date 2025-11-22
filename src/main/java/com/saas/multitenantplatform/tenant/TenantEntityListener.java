package com.saas.multitenantplatform.tenant;

import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

public class TenantEntityListener {

    @PrePersist
    public void prePersist(Object entity) {
        if (entity instanceof TenantAware) {
            String tenant = TenantContext.getTenantId();
            if (tenant == null) {
                throw new IllegalStateException("No tenant in TenantContext for persist");
            }
            ((TenantAware) entity).setTenantKey(tenant);
        }
    }

    @PreUpdate
    public void preUpdate(Object entity) {
        if (entity instanceof TenantAware) {
            // prevent tenant changes — ensure tenantKey remains the same
            String tenant = TenantContext.getTenantId();
            if (tenant == null) {
                throw new IllegalStateException("No tenant in TenantContext for update");
            }
            TenantAware ta = (TenantAware) entity;
            if (!tenant.equals(ta.getTenantKey())) {
                throw new SecurityException("Tenant mismatch on update");
            }
        }
    }
}
