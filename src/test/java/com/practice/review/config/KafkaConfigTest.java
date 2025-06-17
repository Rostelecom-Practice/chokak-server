package com.practice.review.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest(classes = KafkaConfig.class)
@TestPropertySource(properties = {
        "spring.kafka.bootstrap-servers=localhost:9092",
        "spring.kafka.consumer.group-id=test-group",
        "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration"
})
public class KafkaConfigTest {

    @Autowired
    private KafkaConfig kafkaConfig;

    @Test
    public void testRegistryBean() {
        KafkaListenerEndpointRegistry registry = kafkaConfig.registry();
        assertNotNull(registry, "KafkaListenerEndpointRegistry должен быть создан");
    }

    @Test
    public void testConsumerFactoryConfiguration() {
        ConsumerFactory<String, String> consumerFactory = kafkaConfig.consumerFactory();
        assertNotNull(consumerFactory, "ConsumerFactory должен быть создан");

        @SuppressWarnings("unchecked")
        Map<String, Object> configs = (Map<String, Object>) ReflectionTestUtils.getField(
                consumerFactory, "configs");

        assertNotNull(configs, "Конфигурационная карта не должна быть null");
        assertEquals("localhost:9092", configs.get(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG));
        assertEquals("test-group", configs.get(ConsumerConfig.GROUP_ID_CONFIG));
        assertEquals(StringDeserializer.class, configs.get(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG));
        assertEquals(StringDeserializer.class, configs.get(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG));
        assertEquals("earliest", configs.get(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG));
    }

    @Test
    public void testContainerFactoryConfiguration() {
        KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, String>> factory =
                kafkaConfig.containerFactory();
        assertNotNull(factory, "KafkaListenerContainerFactory должен быть создан");

        @SuppressWarnings("unchecked")
        int concurrency = (int) ReflectionTestUtils.getField(factory, "concurrency");
        boolean autoStartup = (boolean) ReflectionTestUtils.getField(factory, "autoStartup");

        assertEquals(1, concurrency);
        assertTrue(autoStartup);

        @SuppressWarnings("unchecked")
        ConsumerFactory<String, String> consumerFactory = (ConsumerFactory<String, String>) ReflectionTestUtils.getField(
                factory, "consumerFactory");

        assertNotNull(consumerFactory, "ConsumerFactory в контейнере не должен быть null");
    }

    @Test
    public void beansAreInjectedCorrectly() {
        assertNotNull(kafkaConfig.registry(), "KafkaListenerEndpointRegistry должен быть внедрен");
        assertNotNull(kafkaConfig.consumerFactory(), "ConsumerFactory должен быть внедрен");
        assertNotNull(kafkaConfig.containerFactory(), "ContainerFactory должен быть внедрен");
    }

}
