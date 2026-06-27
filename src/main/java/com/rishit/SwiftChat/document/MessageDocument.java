package com.rishit.SwiftChat.document;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Document(indexName = "messages")
@Getter
@Setter
public class MessageDocument {

    @Id
    private UUID id;

    @Field(type = FieldType.Keyword)
    private UUID chatId;

    @Field(type = FieldType.Keyword)
    private UUID senderId;

    @Field(type = FieldType.Text)
    private String senderName;

    @Field(type = FieldType.Text)
    private String content;

//    @Field(type = FieldType.Date)
    @Field(
        type = FieldType.Date,
        format = DateFormat.date_hour_minute_second_fraction
    )
    private LocalDateTime createdAt;
}