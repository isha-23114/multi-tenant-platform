package com.saas.multitenantplatform.user;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService svc;

    public UserController(UserService svc) {
        this.svc = svc;
    }

    @GetMapping
    public List<User> list() {
        return svc.listUsersForCurrentTenant();
    }

    @GetMapping("/{id}")
    public User get(@PathVariable Long id) {
        return svc.getUserForCurrentTenant(id);
    }

    @PreAuthorize("hasRole('TENANT_ADMIN') or hasRole('SUPER_ADMIN')")
    @PostMapping
    public ResponseEntity<User> create(@RequestBody UserCreateRequest request) {
        User user = User.builder()
                .email(request.email())
                .fullName(request.fullName())
                .passwordHash(request.passwordHash())
                .role(request.role())
                .build();
        User created = svc.createUserForCurrentTenant(user);
        return ResponseEntity.created(URI.create("/api/users/" + created.getId())).body(created);
    }
}
