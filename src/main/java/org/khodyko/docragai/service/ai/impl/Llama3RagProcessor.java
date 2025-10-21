package org.khodyko.docragai.service.ai.impl;

import lombok.RequiredArgsConstructor;
import org.khodyko.docragai.advisor.DocumentAdvisor;
import org.khodyko.docragai.agent.TestAgent;
import org.khodyko.docragai.service.ai.AiRagProcessor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.document.Document;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class Llama3RagProcessor implements AiRagProcessor {

    private final ChatClient chatClient;
    @Qualifier("openAiChatClient")
    private final ChatClient openAiChatClient;

//    private final VectorStore vectorStore;
    private final TestAgent testAgent;

    @Override
    public String askAi(String query, List<Document> docs){
//        Prompt prompt = new Prompt(
//                query,
//                OllamaOptions.builder()
//                        .model(llamaModel)
//                        .temperature(0.6)
//                        .build()
//        );

        DocumentAdvisor documentAdvisor = new DocumentAdvisor();
        documentAdvisor.setDocuments(docs);
        ChatMemory chatMemory1 = MessageWindowChatMemory.builder()
                .chatMemoryRepository(new InMemoryChatMemoryRepository())
                .maxMessages(10)
                .build();


        var response = chatClient
                .prompt(query) //mainPrompt
//                .system("systemPrompt")
//                .user("123") //userPrompt
                .advisors(documentAdvisor)
//                .advisors(new QuestionAnswerAdvisor(vectorStore))
//                .tools(testAgent)
                .call()
                .content();

        return Optional.ofNullable(response)
                .orElse("Не удалось получить ответ");
    }

}
