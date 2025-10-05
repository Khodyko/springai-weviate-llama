package org.khodyko.docragai.service.db;

import org.khodyko.docragai.vo.RecDocMetaData;
import org.springframework.ai.document.Document;
import org.springframework.core.io.Resource;

import java.util.List;

public interface RecDocCreator {
    List<Document> createRecDocsFromFile(Resource resource, RecDocMetaData recDocMetaData);

    List<Document> createRecDocsFromFile(Resource[] resources);
}
