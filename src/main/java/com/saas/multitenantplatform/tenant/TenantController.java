package com.saas.multitenantplatform.tenant;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tenants")
public class TenantController {
    private final TenantService tenantService;
    public TenantController(TenantService tenantService) {
        this.tenantService = tenantService;
    }

    @GetMapping
    public List<Tenant> getAll() {
        return tenantService.getAllTenants();
    }

    @GetMapping("/{key}")
    public Tenant getByKey(@PathVariable String key) {
        return tenantService.getTenantByKey(key)
                .orElseThrow(() -> new RuntimeException("Tenant not found"));
    }

    @PostMapping
    public Tenant create(@RequestBody Tenant tenant) {
        return tenantService.createTenant(tenant);
    }

}
