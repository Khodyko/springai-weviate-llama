package org.khodyko.docragai.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.ai.transformer.splitter.TextSplitter;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DocumentService {

    private final VectorStore vectorStore;


    public void readDocuments() {
        try {
            PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
            Resource[] resources = resolver.getResources("classpath:articles/*");
            for (Resource resource : resources) {
                TikaDocumentReader reader = new TikaDocumentReader(resource);
                TextSplitter textSplitter = new TokenTextSplitter();
                List<Document> documents = reader.get();
                for (Document doc:documents){
                    doc.getMetadata().put("fileName", resource.getFile().getName());
                }
                vectorStore.accept(textSplitter.apply(documents));
            }

        } catch (Exception e) {
            log.warn("error while read documents",e);
        }
        log.debug("allDocS Readed");
    }

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
}
