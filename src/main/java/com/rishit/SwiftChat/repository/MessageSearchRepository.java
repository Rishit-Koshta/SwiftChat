package com.rishit.SwiftChat.repository;

import com.rishit.SwiftChat.document.MessageDocument;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MessageSearchRepository extends ElasticsearchRepository<MessageDocument, UUID> {
    @Query("""
{
  "bool": {
    "must": [
      { "term": { "chatId": "?0" } },
      { "match": { "content": "?1" } },
      { "term": { "deleted": false } }
    ]
  }
}
""")
    List<MessageDocument> searchMessages(UUID chatId, String keyword);
}