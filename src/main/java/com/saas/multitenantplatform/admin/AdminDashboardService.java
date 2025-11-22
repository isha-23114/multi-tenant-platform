package com.saas.multitenantplatform.admin;

import com.saas.multitenantplatform.tenant.TenantContext;
import com.saas.multitenantplatform.user.UserRepository;
import com.saas.multitenantplatform.project.ProjectRepository;
import com.saas.multitenantplatform.audit.AuditLogRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminDashboardService {

    private final UserRepository userRepo;
    private final ProjectRepository projectRepo;
    private final AuditLogRepository auditRepo;

    public AdminDashboardService(UserRepository userRepo, ProjectRepository projectRepo, AuditLogRepository auditRepo) {
        this.userRepo = userRepo;
        this.projectRepo = projectRepo;
        this.auditRepo = auditRepo;
    }

    public long userCount() {
        return userRepo.countByTenantKey(TenantContext.getTenantId());
    }

    public long projectCount() {
        return projectRepo.countByTenantKey(TenantContext.getTenantId());
    }

    public List<?> recentAudits(int limit) {
        return auditRepo.findTop20ByTenantKeyOrderByChangedAtDesc(TenantContext.getTenantId());
    }
}
