package com.practice.review.config;

import com.practice.review.application.registry.*;
import com.practice.review.core.ReviewCommandService;
import com.practice.review.core.ReviewSource;
import com.practice.review.infra.kafka.KafkaListenerBeanFactory;
import com.practice.review.infra.sources.InternalReviewSourceId;
import com.practice.review.infra.sources.example.TestCityRestaurantsReviewSource;
import com.practice.review.infra.sources.example.TestCommandService;
import com.practice.review.infra.sources.example.TestIngestionAdapter;
import com.practice.review.infra.sources.example.TestSnapshotImporter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;

@Configuration
@ComponentScan
public class RegistryConfig {

    public @Bean TestCityRestaurantsReviewSource testCityRestaurantsReviewSource
            (@Autowired TestSnapshotImporter testSnapshotImporter,
             @Autowired TestCommandService testCommandService,
             @Autowired TestIngestionAdapter testIngestionAdapter) {
        return new TestCityRestaurantsReviewSource(UUID.randomUUID(), testSnapshotImporter, testCommandService,
                testIngestionAdapter);
    }


    public @Bean ReviewSourceRegistry reviewSourceRegistry(@Autowired ReviewSource internalReviewSource,
                                                           @Autowired ReviewSource testCityRestaurantsReviewSource) {
        ReviewSourceRegistry registry = new DefaultReviewSourceRegistry();
        registry.register(internalReviewSource);
        registry.register(testCityRestaurantsReviewSource);
        return registry;
    }

    public @Bean ReviewCommandRegistry reviewCommandRegistry(@Autowired ReviewSource internalReviewSource,
                                                             @Autowired ReviewSource testCityRestaurantsReviewSource) {
        ReviewCommandRegistry registry = new DefaultReviewCommandRegistry();
        registry.register(internalReviewSource.sourceId(), internalReviewSource.commandService().get());
        registry.register(testCityRestaurantsReviewSource.sourceId(), testCityRestaurantsReviewSource.commandService().get());
        return registry;
    }

    public @Bean ReviewIngestionRegistry reviewIngestionRegistry(@Autowired KafkaListenerBeanFactory kafkaListenerBeanFactory,
                                                                 @Autowired TestCityRestaurantsReviewSource testCityRestaurantsReviewSource) {
        ReviewIngestionRegistry registry = new DefaultReviewIngestionRegistry();
        //registry.register(InternalReviewSourceId.VALUE, internalReviewSource.ingestionAdapter().get());
        kafkaListenerBeanFactory.registerReviewListener(testCityRestaurantsReviewSource.ingestionAdapter().get());
        return registry;
    }


}
