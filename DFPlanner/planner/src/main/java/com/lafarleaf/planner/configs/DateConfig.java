package com.lafarleaf.planner.configs;

import java.text.SimpleDateFormat;
import java.util.Locale;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DateConfig {
    @Bean
    public SimpleDateFormat dbDateFormat() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US);
    }
}
