package com.saas.multitenantplatform.web;

import com.saas.multitenantplatform.tenant.TenantContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class TenantPingController {
    @GetMapping("/tenant-info")
    public Map<String, String> tenantInfo() {
        String tenant = TenantContext.getTenantId();
        return Map.of("tenant", tenant == null ? "null" : tenant);
    }
}
