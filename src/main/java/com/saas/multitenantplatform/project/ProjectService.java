package com.saas.multitenantplatform.project;

import com.saas.multitenantplatform.tenant.TenantContext;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProjectService {

    private final ProjectRepository repo;

    public ProjectService(ProjectRepository repo) {
        this.repo = repo;
    }

    public Project create(String name, String description) {
        String tenant = TenantContext.getTenantId();
        if (tenant == null) throw new IllegalStateException("No tenant");
        Project p = Project.builder().name(name).description(description).build();
        p.setTenantKey(tenant);
        return repo.save(p);
    }

    public List<Project> listForCurrentTenant() {
        return repo.findAllByTenantKey(TenantContext.getTenantId());
    }
}
