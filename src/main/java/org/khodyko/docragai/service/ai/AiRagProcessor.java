package org.khodyko.docragai.service.ai;

import org.springframework.ai.document.Document;

import java.util.List;

public interface AiRagProcessor {
    List<String> getSynonymQueries(String query, Integer countOfCopy);

    String prepareQueryForVectorSearch(String query);

    String makeConclusionByDocData(String query, List<Document> docs);

    String askAi(String query);
}
