package edu.java.configuration;

import lombok.Getter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
@EnableConfigurationProperties(ApplicationConfig.class)
public class LinkSchedulerConfiguration {
    private final ApplicationConfig applicationConfig;

    public LinkSchedulerConfiguration(ApplicationConfig applicationConfig) {
        this.applicationConfig = applicationConfig;
    }
}
