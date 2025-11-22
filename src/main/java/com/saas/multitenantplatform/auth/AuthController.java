package com.saas.multitenantplatform.auth;

import com.saas.multitenantplatform.security.JwtUtil;
import com.saas.multitenantplatform.user.User;
import com.saas.multitenantplatform.user.UserRepository;
import com.saas.multitenantplatform.tenant.TenantContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authManager;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepo;

    public AuthController(@Lazy AuthenticationManager authManager, JwtUtil jwtUtil, UserRepository userRepo) {
        this.authManager = authManager;
        this.jwtUtil = jwtUtil;
        this.userRepo = userRepo;
    }

    record LoginRequest(String email, String password) {}

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest body, @RequestHeader(value = "X-Tenant-ID", required = false) String headerTenant) {
        // TenantContext should already be set by TenantFilter; if not, allow header here as fallback:
        String tenant = TenantContext.getTenantId();
        if (tenant == null && headerTenant != null && !headerTenant.isBlank()) {
            TenantContext.setTenantId(headerTenant);
            tenant = headerTenant;
        }

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(body.email(), body.password());
        Authentication auth = authManager.authenticate(token); // will call UserDetailsServiceImpl

        // find user to include subject info (id or email)
        User user = userRepo.findByEmailAndTenantKey(body.email(), tenant).orElseThrow();

        String jwt = jwtUtil.generateToken(String.valueOf(user.getId()), tenant, user.getEmail());

        return ResponseEntity.ok(Map.of("token", jwt));
    }
}
