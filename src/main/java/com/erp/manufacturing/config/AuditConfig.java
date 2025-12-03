package com.erp.manufacturing.config;

import com.erp.manufacturing.security.UserPrincipal;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@Configuration
public class AuditConfig {

    @Bean
    public AuditorAware<Long> auditorProvider() {
        return () -> {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            
            if (authentication == null || 
                !authentication.isAuthenticated() || 
                authentication instanceof AnonymousAuthenticationToken) {
                return Optional.empty();
            }
            
            Object principal = authentication.getPrincipal();
            if (principal instanceof UserPrincipal) {
                return Optional.of(((UserPrincipal) principal).getId());
            }
            
            return Optional.empty();
        };
    }
}

