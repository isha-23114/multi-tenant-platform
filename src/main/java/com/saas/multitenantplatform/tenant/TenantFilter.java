package com.saas.multitenantplatform.tenant;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class TenantFilter extends OncePerRequestFilter {

    private static final String TENANT_HEADER = "X-Tenant-ID";
    private static final String DEFAULT_TENANT = "public";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String tenantId = resolveTenantId(request);
            TenantContext.setTenantId(tenantId);
            filterChain.doFilter(request, response);
        }
        finally {
            TenantContext.clear();
        }
    }

    private String resolveTenantId(HttpServletRequest request) {
        String header = request.getHeader(TENANT_HEADER);
        if (header != null && !header.isBlank()) {
            return header.trim();
        }
        //parse subdomain from host header
        String host = request.getHeader("Host");
        if (host != null && !host.isBlank()) {
            // strip port if present
            String hostOnly = host.split(":")[0];
            String[] parts = hostOnly.split("\\.");
            if (parts.length >= 2) {
                // if host = acme.example.com -> tenant = first label (acme)
                // if host = acme.localhost -> tenant = first label (acme)
                return parts[0].trim();
            }
        }

        return DEFAULT_TENANT;
    }
}
