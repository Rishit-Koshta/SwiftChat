package com.rishit.SwiftChat.messaging.consumer;
import com.rishit.SwiftChat.config.RabbitMQConfig;
import com.rishit.SwiftChat.dto.event.NewMessageEvent;
import com.rishit.SwiftChat.document.MessageDocument;
import com.rishit.SwiftChat.repository.MessageSearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RabbitMQConsumer {

    private final MessageSearchRepository searchRepository;

    // This method constantly listens to the queue in the background
    @RabbitListener(queues = RabbitMQConfig.SEARCH_QUEUE)
    public void consumeMessageEvent(NewMessageEvent event) {

        System.out.println("Received message from RabbitMQ! Indexing to Elasticsearch...");

        // 1. Map the Event DTO to the Elasticsearch Document
        MessageDocument document = new MessageDocument();
        document.setId(event.getMessageId());
        document.setChatId(event.getChatId());
        document.setSenderId(event.getSenderId());
        document.setSenderName(event.getSenderName());
        document.setContent(event.getContent());
        document.setCreatedAt(event.getCreatedAt());

        // 2. Save it to Elasticsearch
        searchRepository.save(document);

        System.out.println("Successfully indexed message ID: " + event.getMessageId());
    }
}
