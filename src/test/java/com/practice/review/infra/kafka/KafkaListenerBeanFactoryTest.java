package com.practice.review.infra.kafka;

import com.practice.review.infra.kafka.ReviewKafkaListener.ReviewIngestionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.config.MethodKafkaListenerEndpoint;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class KafkaListenerBeanFactoryTest {

    @Mock
    private KafkaListenerEndpointRegistry registry;

    @Mock
    private KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, String>> containerFactory;

    private KafkaListenerBeanFactory factory;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        factory = new KafkaListenerBeanFactory(registry, containerFactory);

        setPrivateField("reviewTopic", "topic-xyz");
        setPrivateField("groupId",     "group-abc");
    }

    private void setPrivateField(String name, String value) throws Exception {
        Field field = ReflectionUtils.findField(KafkaListenerBeanFactory.class, name);
        ReflectionUtils.makeAccessible(field);
        field.set(factory, value);
    }

    @Test
    void registerReviewListener_registersEndpointWithCorrectProperties() throws NoSuchMethodException {
        ReviewIngestionHandler handler = details -> { /* noop */ };

        factory.registerReviewListener(handler);

        ArgumentCaptor<MethodKafkaListenerEndpoint<String, String>> captor =
                ArgumentCaptor.forClass(MethodKafkaListenerEndpoint.class);
        verify(registry).registerListenerContainer(captor.capture(), eq(containerFactory), eq(true));

        MethodKafkaListenerEndpoint<String, String> endpoint = captor.getValue();

        assertThat(endpoint.getId()).startsWith("review-listener-");

        assertThat(endpoint.getGroupId()).isEqualTo("group-abc");
        assertThat(endpoint.getTopics()).containsExactly("topic-xyz");

        Object bean = endpoint.getBean();
        assertThat(bean).isInstanceOf(ReviewKafkaListener.class);

        Method expectedMethod = ReflectionUtils.findMethod(ReviewKafkaListener.class, "consume", String.class);
        assertThat(endpoint.getMethod()).isEqualTo(expectedMethod);
    }
}
