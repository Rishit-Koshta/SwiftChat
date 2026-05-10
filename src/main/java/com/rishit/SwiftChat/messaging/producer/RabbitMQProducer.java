package com.rishit.SwiftChat.messaging.producer;

import com.rishit.SwiftChat.config.RabbitMQConfig;
import com.rishit.SwiftChat.dto.event.NewMessageEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RabbitMQProducer {

    private final RabbitTemplate rabbitTemplate;

    public void sendToSearchQueue(NewMessageEvent event) {
        // drop the message into the Exchange, and tell it to use the "message.created" routing key.
        // RabbitMQ instantly routes it to the search_sync_queue!
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE_NAME,
                RabbitMQConfig.ROUTING_KEY,
                event
        );
        System.out.println("Message event published to RabbitMQ! Message ID: " + event.getMessageId());
    }
}