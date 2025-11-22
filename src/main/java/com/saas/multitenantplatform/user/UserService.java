package com.saas.multitenantplatform.user;

import com.saas.multitenantplatform.tenant.TenantContext;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {

    private final UserRepository repo;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository repo, PasswordEncoder passwordEncoder) {
        this.repo = repo;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> listUsersForCurrentTenant() {
        String tenant = currentTenant();
        return repo.findAllByTenantKey(tenant);
    }

    @Transactional
    public User createUserForCurrentTenant(User user) {
        String tenant = currentTenant();
        // ensure we don't allow client to set tenantKey
        user.setTenantKey(tenant);
        if (user.getRole() == null) user.setRole("TENANT_USER");
        // If client passed a password in passwordHash (dev convenience),
        // detect if it's already a bcrypt hash; otherwise encode it.
        System.out.println("DEBUG createUser: email=" + user.getEmail() + " pwPresent=" + (user.getPasswordHash() != null));
        String pw = user.getPasswordHash();
        if (pw != null && !pw.isBlank()) {
            if (!looksLikeBcrypt(pw)) {
                user.setPasswordHash(passwordEncoder.encode(pw));
            } // else assume already hashed
        }
        return repo.save(user);
    }

    public User getUserForCurrentTenant(Long id) {
        String tenant = currentTenant();
        return repo.findByIdAndTenantKey(id, tenant)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    private String currentTenant() {
        String t = TenantContext.getTenantId();
        if (t == null) throw new IllegalStateException("No tenant in context");
        return t;
    }

    private boolean looksLikeBcrypt(String s) {
        // bcrypt hashes typically start with $2a$ or $2b$ or $2y$
        return s.startsWith("$2a$") || s.startsWith("$2b$") || s.startsWith("$2y$");
    }
}
