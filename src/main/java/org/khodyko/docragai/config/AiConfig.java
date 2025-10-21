package org.khodyko.docragai.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.ollama.OllamaEmbeddingModel;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiEmbeddingModel;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.ai.retry.TransientAiException;
import org.springframework.ai.vectorstore.weaviate.WeaviateVectorStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.RetryListener;
import org.springframework.retry.support.RetryTemplate;

import java.time.Duration;

import static org.apache.xmpbox.schema.TiffSchema.MODEL;

@Slf4j
@Configuration
@PropertySource("classpath:env.properties")
@RequiredArgsConstructor
public class AiConfig {

    @Value("${spring.ai.openai.api-key}")
    private final String openAiApiKey;


    @Bean("openAiChatClient")
    public ChatClient openAiChatClient(@Qualifier("openAiChatModel1") ChatModel chatModel) {
        ChatClient openAiChatClient = ChatClient.builder(chatModel).build();
        return openAiChatClient;
    }

    @Bean("openAiChatModel1")
    public ChatModel chatModel() {
        var openAiApi = OpenAiApi.builder().apiKey(openAiApiKey).build();
        return OpenAiChatModel.builder()
                .openAiApi(openAiApi)
                .build();
    }

    @Bean("llamaModel")
    @Primary
    public ChatModel chatModelLlama() {
        var ollamaApi = OllamaApi.builder().build();
        return OllamaChatModel.builder()
                .ollamaApi(ollamaApi)
                .defaultOptions(
                        OllamaOptions.builder()
                                .model("llama3:8b")
                                .temperature(0.9)
                                .build())
                .build();
    }

    @Bean
    @Primary
    public EmbeddingModel embeddingModel() {
        var openAiApi = OpenAiApi.builder().apiKey(openAiApiKey).build();
        return new OpenAiEmbeddingModel(openAiApi);
    }



    @Bean("llamaChatClient")
    @Primary
    public ChatClient llamaChatClient(@Qualifier("llamaModel") ChatModel chatModel) {
        return ChatClient.builder(chatModel).build();
    }

}