package com.saas.multitenantplatform.admin;

import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminDashboardService svc;

    public AdminController(AdminDashboardService svc) {
        this.svc = svc;
    }

    @PreAuthorize("hasRole('TENANT_ADMIN') or hasRole('SUPER_ADMIN')")
    @GetMapping("/dashboard")
    public Map<String,Object> dashboard() {
        return Map.of(
                "userCount", svc.userCount(),
                "projectCount", svc.projectCount(),
                "recentAudits", svc.recentAudits(20)
        );
    }

    @PreAuthorize("hasRole('TENANT_ADMIN') or hasRole('SUPER_ADMIN')")
    @GetMapping("/audits")
    public Object audits(@RequestParam(defaultValue = "20") int limit) {
        return svc.recentAudits(limit);
    }
}
