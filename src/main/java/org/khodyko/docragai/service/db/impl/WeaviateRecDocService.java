package org.khodyko.docragai.service.db.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.khodyko.docragai.service.db.RecDocService;
import org.khodyko.docragai.service.db.RecDocCreator;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class WeaviateRecDocService implements RecDocService {

    private final VectorStore vectorStore;
    private final RecDocCreator recDocCreator;

    @Override
    public void readDocumentsFromResourceFolder() {
        try {
            PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
            Resource[] resources = resolver.getResources("classpath:articles/*");
            List<Document> recDocs = recDocCreator.createRecDocsFromFile(resources);
            vectorStore.accept(recDocs);
            log.debug("all docs from folder are loaded");
        } catch (Exception e) {
            log.error("Unexpected exception occure", e);
        }
    }

    @Override
    public void cleanAllDocuments() {
        List<Document> docs = vectorStore.similaritySearch("");
        while (!docs.isEmpty()) {
            for (Document doc : docs) {
                vectorStore.delete("id == '" + doc.getId() + "'");
            }
            docs = vectorStore.similaritySearch("");
        }
        log.debug("everything deleted");
    }

    @Override
    public List<Document> searchRecDocs(String query){
//        FilterExpressionBuilder b = new FilterExpressionBuilder();
//        Filter.Expression expression = b.eq("fileName", "01-01-checks-the-ai-powered-data-protection-project-incubated-in-area-120-officially-exits-to-google.txt").build();

        SearchRequest searchRequest = SearchRequest.builder()
                .query(query)
                .topK(4)
                .similarityThreshold(0.6d)
//                .filterExpression(expression)
                .build();
        List<Document> docs = vectorStore.similaritySearch(searchRequest);
        return docs;
    }
}
