package com.saas.multitenantplatform.tenant;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@Getter
@Setter
public abstract class TenantAware {
    @Column(name = "tenant_key", nullable = false, updatable = false)
    private String tenantKey;
}
