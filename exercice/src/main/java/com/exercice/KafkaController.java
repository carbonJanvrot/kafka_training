package com.exercice;

import com.exercice.utils.PracticalAdvice;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class KafkaController {

    private static final Logger logger = LoggerFactory.getLogger(KafkaController.class);

    private final KafkaTemplate<String, Object> template;
    private final String topicName;
    private final int messagesPerRequest;
    private final CountDownLatch countDownLatch;

    public KafkaController(final KafkaTemplate<String, Object> template,
                           @Value("${exercice.topic-name}") final String topicName,
                           @Value("${exercice.messages-per-request}") final int messagesPerRequest, CountDownLatch countDownLatch) {
        this.template = template;
        this.topicName = topicName;
        this.messagesPerRequest = messagesPerRequest;
        this.countDownLatch = countDownLatch;
    }

    @GetMapping("/hello")
    public String hello() throws InterruptedException {
        IntStream.range(0, messagesPerRequest)
                .forEach(i -> this.template.send(topicName, String.valueOf(i),
                        new PracticalAdvice("A Practical Advice", i)));
        countDownLatch.await(60, TimeUnit.SECONDS);
        logger.info("All messages received");
        return "Hello Kafka!";
    }
}
