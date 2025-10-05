package org.khodyko.docragai.controller;

import lombok.RequiredArgsConstructor;
import org.khodyko.docragai.service.ai.AiRagProcessor;
import org.khodyko.docragai.service.db.RecDocService;
import org.khodyko.docragai.vo.AiRecDocResponse;
import org.springframework.ai.document.Document;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
public class RecDocController {

    private final RecDocService recDocService;
    private final AiRagProcessor aiRagProcessor;

    public void clearDataBase() {
        recDocService.cleanAllDocuments();
    }

    public void fillDbFromFolder() {
        recDocService.readDocumentsFromResourceFolder();
    }

    @PostMapping("/search")
    public AiRecDocResponse searchInVectorDb(@RequestBody Map<String, String> request) {
        String query = request.get("query");
//        List<Document> docs = recDocService.searchRecDocs(query);
        List<Document> docs = new ArrayList<>();
        String aiResponse = aiRagProcessor.askAi(query);
        return new AiRecDocResponse(docs, aiResponse);
    }
}
