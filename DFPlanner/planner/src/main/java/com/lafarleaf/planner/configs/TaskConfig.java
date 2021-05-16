package com.lafarleaf.planner.configs;

import com.lafarleaf.planner.utils.TaskCodeGenerator;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TaskConfig {
    @Bean
    public TaskCodeGenerator taskCodeGenerator() {
        return new TaskCodeGenerator();
    }
}
