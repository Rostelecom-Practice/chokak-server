package com.practice.review.config;

import com.practice.review.application.registry.*;
import com.practice.review.core.ReviewCommandService;
import com.practice.review.core.ReviewSource;
import com.practice.review.infra.sources.InternalReviewSourceId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan
public class RegistryConfig {


    public @Bean ReviewSourceRegistry reviewSourceRegistry(@Autowired ReviewSource internalReviewSource) {
        ReviewSourceRegistry registry = new DefaultReviewSourceRegistry();
        registry.register(internalReviewSource);
        return registry;
    }

    public @Bean ReviewCommandRegistry reviewCommandRegistry(@Autowired ReviewSource internalReviewSource) {
        ReviewCommandRegistry registry = new DefaultReviewCommandRegistry();
        registry.register(InternalReviewSourceId.VALUE, internalReviewSource.commandService().get());
        return registry;
    }

    public @Bean ReviewIngestionRegistry reviewIngestionRegistry(@Autowired ReviewSource internalReviewSource) {
        ReviewIngestionRegistry registry = new DefaultReviewIngestionRegistry();
        //registry.register(InternalReviewSourceId.VALUE, internalReviewSource.ingestionAdapter().get());
        return registry;
    }


}
