package org.khodyko.docragai.service.ai.impl;

import lombok.RequiredArgsConstructor;
import org.khodyko.docragai.service.ai.AiRagProcessor;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.document.Document;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class Llama3RagProcessor implements AiRagProcessor {

    private final OllamaChatModel chatModel;
    @Value("${spring.ai.ollama.chat.model}")
    private String llamaModel;

    private final String CREATE_COPY_OF_QUERY_REQUEST="";
    private final String PREPARE_QUERY_FOR_DB_REQUEST="";

    @Override
    public List<String> getSynonymQueries(String query, Integer countOfCopy){
        ChatResponse response = chatModel.call(
                new Prompt(
                        query,
                        OllamaOptions.builder()
                                .model(llamaModel)
                                .temperature(0.6)
                                .build()
                ));
        //validate
        //Create list of queries
        return null;
    }

    @Override
    public String prepareQueryForVectorSearch(String query){
        ChatResponse response = chatModel.call(
                new Prompt(
                        query,
                        OllamaOptions.builder()
                                .model(llamaModel)
                                .temperature(0.6)
                                .build()
                ));
        return null;
    }

    @Override
    public String makeConclusionByDocData(String query, List<Document> docs){
        ChatResponse response = chatModel.call(
                new Prompt(
                        query,
                        OllamaOptions.builder()
                                .model(llamaModel)
                                .temperature(0.6)
                                .build()
                ));
        return "";
    }

    @Override
    public String askAi(String query){
        ChatResponse response = chatModel.call(
                new Prompt(
                        query,
                        OllamaOptions.builder()
                                .model(llamaModel)
                                .temperature(0.6)
                                .build()
                ));

        return Optional.ofNullable(response)
                .map(ChatResponse::getResult)
                .map(Generation::getOutput)
                .map(AssistantMessage::getText)
                .orElse("Не удалось получить ответ");
    }

}
