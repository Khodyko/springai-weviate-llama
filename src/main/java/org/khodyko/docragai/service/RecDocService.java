package org.khodyko.docragai.service;

import org.springframework.ai.document.Document;

import java.util.List;

public interface RecDocService {
    void readDocumentsFromResourceFolder();

    void cleanAllDocuments();

    List<Document> searchRecDocs(String query);
}
