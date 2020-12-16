package com.rsegador.workshop.testersservice;

import au.com.dius.pact.provider.PactVerifyProvider;
import au.com.dius.pact.provider.junit5.MessageTestTarget;
import au.com.dius.pact.provider.junit5.PactVerificationContext;
import au.com.dius.pact.provider.junitsupport.Provider;
import au.com.dius.pact.provider.junitsupport.loader.PactBroker;
import au.com.dius.pact.provider.spring.junit5.PactVerificationSpringProvider;
import com.rsegador.workshop.testersservice.logs.HttpCallLog;
import com.rsegador.workshop.testersservice.logs.HttpCallLogPublisher;
import lombok.SneakyThrows;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.ContainerTestUtils;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import static com.rsegador.workshop.testersservice.TestersServiceProducerPactTest.TestData.testMessage;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

@EmbeddedKafka(topics = "testers-service-activity",
        bootstrapServersProperty = "spring.kafka.bootstrap-servers")
@TestInstance(PER_CLASS)
@ExtendWith(SpringExtension.class)
@Provider("testers-service")
@PactBroker
@SpringBootTest
@ActiveProfiles("test")
@Disabled
public class TestersServiceProducerPactTest {

    @Autowired
    private EmbeddedKafkaBroker embeddedKafkaBroker;

    @Autowired
    private HttpCallLogPublisher httpCallLogPublisher;

    BlockingQueue<ConsumerRecord<String, String>> records;
    KafkaMessageListenerContainer<String, String> container;

    @BeforeEach
    void before(PactVerificationContext context) {
        context.setTarget(new MessageTestTarget());
    }

    @TestTemplate
    @ExtendWith(PactVerificationSpringProvider.class)
    void testTemplate(PactVerificationContext context) {
        context.verifyInteraction();
    }

    @BeforeAll
    void setUp() {
        // set up a consumer to read the message we will send in the test with a string deserializer. This string is the one send to Pact for the contract verification.
        Map<String, Object> configs = new HashMap<>(KafkaTestUtils.consumerProps("Consumer","false", embeddedKafkaBroker));
        DefaultKafkaConsumerFactory<String, String> consumerFactory = new DefaultKafkaConsumerFactory<>(configs, new StringDeserializer(), new StringDeserializer());
        ContainerProperties containerProperties = new ContainerProperties("testers-service-activity");
        container = new KafkaMessageListenerContainer<>(consumerFactory, containerProperties);
        records = new LinkedBlockingQueue<>();
        container.setupMessageListener((MessageListener<String, String>) records::add);
        container.start();
        ContainerTestUtils.waitForAssignment(container, embeddedKafkaBroker.getPartitionsPerTopic());
    }

    @AfterAll
    void tearDown() {
        container.stop();
    }

    @SneakyThrows
    @PactVerifyProvider("valid activity from testers service")
    public String verifyHttpLogActivityMessageSend() {

        // Send a real application message
        httpCallLogPublisher.sendLogMessage(testMessage);

        // receive the value from the consumer configured in the set up.
        ConsumerRecord<String, String> singleRecord = records.poll(100, TimeUnit.MILLISECONDS);
        assertThat(singleRecord).isNotNull();

        // return the value to Pact
        return singleRecord.value();
    }

    interface TestData {
        HttpCallLog testMessage = HttpCallLog.builder().path("/path/test").localDateTime(LocalDateTime.now()).build();
    }

}


