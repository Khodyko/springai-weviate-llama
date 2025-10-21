package org.khodyko.docragai.service.ai;

import org.springframework.ai.document.Document;

import java.util.List;

public interface AiRagProcessor {

    String askAi(String query, List<Document> docs);
}
