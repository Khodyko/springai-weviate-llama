package org.khodyko.docragai.service.db.impl;

import org.khodyko.docragai.service.db.RecDocCreator;
import org.khodyko.docragai.vo.RecDocMetaData;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.ai.transformer.splitter.TextSplitter;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class WeaviateRecDocCreator implements RecDocCreator {

    @Override
    public List<Document> createRecDocsFromFile(Resource resource, RecDocMetaData recDocMetaData) {
        TikaDocumentReader reader = new TikaDocumentReader(resource);
        TextSplitter textSplitter = new TokenTextSplitter(100, 10, 20, 10000, true);
        List<Document> documents = reader.get();
        for (Document doc : documents) {
            doc.getMetadata().putAll(recDocMetaData.getMetaDataMap());
        }
        return textSplitter.apply(documents);
    }

    @Override
    public List<Document> createRecDocsFromFile(Resource[] resources) {
        try {
            List<Document> result = new ArrayList<>();
            RecDocMetaData currentMetaData;
            for (Resource resource : resources) {
                currentMetaData = new RecDocMetaData();
                currentMetaData.setUid(resource.getFile().getName());
                result.addAll(createRecDocsFromFile(resource, currentMetaData));
            }
            return result;
        } catch (IOException e){
            throw new RuntimeException(e);
        }
    }
}
