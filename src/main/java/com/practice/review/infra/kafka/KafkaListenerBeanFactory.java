package com.practice.review.infra.kafka;

import com.practice.review.core.ReviewDetails;
import com.practice.review.infra.kafka.ReviewKafkaListener.ReviewIngestionHandler;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.config.MethodKafkaListenerEndpoint;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.adapter.RecordMessagingMessageListenerAdapter;
import org.springframework.kafka.listener.adapter.MessagingMessageListenerAdapter;
import org.springframework.kafka.listener.adapter.HandlerAdapter;
import org.springframework.kafka.support.converter.MessagingMessageConverter;
import org.springframework.kafka.support.converter.StringJsonMessageConverter;
import org.springframework.messaging.handler.invocation.InvocableHandlerMethod;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.UUID;

@Component
public class KafkaListenerBeanFactory {

    private final KafkaListenerEndpointRegistry registry;
    private final KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, String>> containerFactory;

    @Value("${review.kafka.topic}")
    private String reviewTopic;

    @Value("${spring.kafka.consumer.group-id}")
    private String groupId;

    public KafkaListenerBeanFactory(
            KafkaListenerEndpointRegistry registry,
            KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, String>> containerFactory
    ) {
        this.registry = registry;
        this.containerFactory = containerFactory;
    }

    public void registerReviewListener(ReviewIngestionHandler handler) {
        ReviewKafkaListener listener = new ReviewKafkaListener(handler);

        Method method = ReflectionUtils.findMethod(ReviewKafkaListener.class, "consume", String.class);
        if (method == null) {
            throw new IllegalStateException("Method consume not found");
        }

        MethodKafkaListenerEndpoint<String, String> endpoint = new MethodKafkaListenerEndpoint<>();
        endpoint.setId("review-listener-" + UUID.randomUUID());
        endpoint.setGroupId(groupId);
        endpoint.setTopics(reviewTopic);
        endpoint.setBean(listener);
        endpoint.setMethod(method);
        endpoint.setMessageHandlerMethodFactory((bean, methodToCall) -> new InvocableHandlerMethod(bean, methodToCall));

        registry.registerListenerContainer(endpoint, containerFactory, true);
    }
}
