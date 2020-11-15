package com.lafarleaf.leafplanner.configurations;

import java.util.concurrent.TimeUnit;

import com.lafarleaf.leafplanner.services.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class AppSecurityConfig extends WebSecurityConfigurerAdapter {
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;

    @Autowired
    public AppSecurityConfig(PasswordEncoder passwordEncoder, UserService userService) {
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http // HTTP Request
                .csrf().disable() // Not needed for this project.
                .authorizeRequests() // Start authorize request.
                .antMatchers("/leafplanner/api/user/all", "/leafplanner/api/user/add").permitAll() // Public URL
                .antMatchers("/leafplanner/api/**").authenticated() // Only if Authenticated
                .anyRequest().authenticated() // Any request must be authenticated.
                .and().formLogin() // Login settings.
                .usernameParameter("email") // Username Parameter set to email.
                .and().rememberMe() // Remember me settings.
                .tokenValiditySeconds((int) TimeUnit.DAYS.toSeconds(30)) // Token active for 30 days.
                .key("laFarLeafPlanner") // Encrypted key.
                .and().logout() // Logout settings.
                .clearAuthentication(true) // Clear authentication.
                .invalidateHttpSession(true) // Clear Http sessions.
                .deleteCookies("JSESSIONID", "remember-me"); // Delete cookies.
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(daoAuthenticationProvider());
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsService(userService);
        return provider;
    }
}
