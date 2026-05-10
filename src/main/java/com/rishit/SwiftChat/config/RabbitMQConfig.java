package com.rishit.SwiftChat.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE_NAME = "chat_exchange";
    public static final String SEARCH_QUEUE = "search_sync_queue";
    public static final String ROUTING_KEY = "message.created";

    // define the Queue
    @Bean
    public Queue searchQueue() {
        return new Queue(SEARCH_QUEUE, true); // true = durable (survives server restarts)
    }

    // define the Exchange (TopicExchange allows wildcard routing if needed later)
    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }

    // bind the Queue to the Exchange using the Routing Key
    @Bean
    public Binding binding(Queue searchQueue, TopicExchange exchange) {
        return BindingBuilder.bind(searchQueue).to(exchange).with(ROUTING_KEY);
    }

    // message Converter (Crucial: Converts your Java Objects to JSON before sending)
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    // apply the converter to the RabbitTemplate
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jsonMessageConverter());
        return template;
    }
}
