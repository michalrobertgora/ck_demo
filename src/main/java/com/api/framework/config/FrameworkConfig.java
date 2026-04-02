package com.api.framework.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Root Spring configuration — component-scans the com.api package tree.
 * Test classes reference this as their context configuration entry point.
 */
@Configuration
@ComponentScan(basePackages = "com.api")
public class FrameworkConfig {
}
