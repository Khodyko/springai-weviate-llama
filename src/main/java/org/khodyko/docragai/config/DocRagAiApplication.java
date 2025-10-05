package org.khodyko.docragai.config;

import lombok.RequiredArgsConstructor;
import org.khodyko.docragai.service.DocumentService;
import org.springframework.ai.document.Document;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.filter.Filter;
import org.springframework.ai.vectorstore.filter.FilterExpressionBuilder;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

import java.util.List;

@SpringBootApplication(scanBasePackages = "org.khodyko.docragai", exclude = {DataSourceAutoConfiguration.class})
@RequiredArgsConstructor
public class DocRagAiApplication implements CommandLineRunner {

    private final OllamaChatModel chatModel;
    private final DocumentService documentService;
    private final VectorStore vectorStore;


    public static void main(String[] args) {
        SpringApplication.run(DocRagAiApplication.class, args);
    }


    @Override
    public void run(String... args) {
//        String promptText = "Привет! Объясни коротко, что делает модель Llama3?";
//        ChatResponse response = chatModel.call(
//                new Prompt(
//                        promptText,
//                        OllamaOptions.builder()
//                                .model("llama3:8b")
//                                .temperature(0.4)
//                                .build()
//                ));
//
//        System.out.println(response);

//        documentService.readDocuments();


//        documentService.cleanAllDocuments();


        FilterExpressionBuilder b = new FilterExpressionBuilder();
        Filter.Expression expression = b.eq("fileName", "01-01-checks-the-ai-powered-data-protection-project-incubated-in-area-120-officially-exits-to-google.txt").build();

        SearchRequest searchRequest = SearchRequest.builder()
                .query("Partners of 3one4 Capital, a venture capital firm in India, recently went on a road")
                .topK(4)
                .similarityThreshold(0.6d)
//                .filterExpression(expression)
                .build();
        List<Document> docs=vectorStore.similaritySearch(searchRequest);
        IO.println(docs);
    }
}
