package com.lafarleaf.leafguard.configurations;

import com.lafarleaf.leafguard.utils.enums.AppUserPermission;
import com.lafarleaf.leafguard.utils.enums.AppUserRole;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
@EnableWebSecurity
public class AppSecurityConfig extends WebSecurityConfigurerAdapter {
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AppSecurityConfig(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http // HTTP Request
                .csrf().disable() // TODO: To learn later...
                .authorizeRequests() // Start authorize request.
                .antMatchers("/", "index").permitAll() // URL in antMatchers() are public, means no authentication.
                .antMatchers("/api/v1/**").hasRole(AppUserRole.ADMIN.name()) // URL in the antMatchers() need roles.
                .antMatchers(HttpMethod.DELETE, "/mangement/api/v1/**")
                .hasAuthority(AppUserPermission.COURSE_WRITE.name()) // URL with permission.

                .anyRequest().authenticated() // Any request must be authenticated.
                .and().httpBasic(); // Using HTTP basic.
    }

    @Override
    @Bean
    protected UserDetailsService userDetailsService() {
        UserDetails admin = User.builder() // User builder.
                .username("dleong") // Username
                .password(passwordEncoder.encode("admin-leong")) // Password
                // .roles(AppUserRole.ADMIN.name()) // Role
                .authorities(AppUserRole.ADMIN.getGrantedAuthorities()) // ADMIN authority.
                .build();
        return new InMemoryUserDetailsManager(admin);
    }
}
