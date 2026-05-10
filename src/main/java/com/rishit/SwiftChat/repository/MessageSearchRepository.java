package com.rishit.SwiftChat.repository;

import com.rishit.SwiftChat.document.MessageDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MessageSearchRepository extends ElasticsearchRepository<MessageDocument, UUID> {
    List<MessageDocument> findByChatIdAndContentMatches(UUID chatId, String keyword);
}