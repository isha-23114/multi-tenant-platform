package com.saas.multitenantplatform.tenant;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TenantService {
    private final TenantRepository tenantRepository;

    public TenantService(TenantRepository tenantRepository) {
        this.tenantRepository = tenantRepository;
    }
    public List<Tenant> getAllTenants() {
        return tenantRepository.findAll();
    }
    public Optional<Tenant> getTenantByKey(String tenantKey) {
        return tenantRepository.findByTenantKey(tenantKey);
    }
    public Tenant createTenant(Tenant tenant) {
        return tenantRepository.save(tenant);
    }
}
