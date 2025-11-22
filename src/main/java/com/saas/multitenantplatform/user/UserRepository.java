package com.saas.multitenantplatform.user;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findAllByTenantKey(String tenantKey);
    Optional<User> findByIdAndTenantKey(Long id, String tenantKey);
    Optional<User> findByEmailAndTenantKey(String email, String tenantKey);
    Long countByTenantKey(String tenantId);
}
