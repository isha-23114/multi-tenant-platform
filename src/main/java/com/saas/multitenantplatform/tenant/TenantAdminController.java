package com.saas.multitenantplatform.tenant;

import com.saas.multitenantplatform.user.User;
import com.saas.multitenantplatform.user.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tenant-admin")
public class TenantAdminController {

    private final UserRepository userRepo;

    public TenantAdminController(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    // promote (only super admin to tenant admin for now)
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @PostMapping("/promote/{userId}")
    public ResponseEntity<?> promoteToTenantAdmin(@PathVariable Long userId) {
        User u = userRepo.findById(userId).orElseThrow();
        u.setRole("TENANT_ADMIN");
        userRepo.save(u);
        return ResponseEntity.ok().build();
    }
}
