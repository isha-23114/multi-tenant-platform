package com.saas.multitenantplatform.security;

import com.saas.multitenantplatform.user.User;
import com.saas.multitenantplatform.user.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import com.saas.multitenantplatform.tenant.TenantContext;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;
    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    //Note: we will call this with tenant aware context (tenant must be set)
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
        //username here is email, tenant must be in tenant context
        String tenant = TenantContext.getTenantId();
        if(tenant == null){
            throw new UsernameNotFoundException("Tenant not set in context. Tenant not found");
        }

        User user = userRepository.findByEmailAndTenantKey(username, tenant)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole()));

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPasswordHash() == null ? "" : user.getPasswordHash(),
                true, true, true, true,
                authorities
        );
    }
}
