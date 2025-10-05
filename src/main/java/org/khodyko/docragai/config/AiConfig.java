package org.khodyko.docragai.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.ai.retry.TransientAiException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.RetryListener;
import org.springframework.retry.support.RetryTemplate;

import java.time.Duration;

import static org.apache.xmpbox.schema.TiffSchema.MODEL;

@Slf4j
@Configuration
public class AiConfig {

    @Bean
    public OllamaChatModel ollamaChat(OllamaApi ollamaApi) {
        RetryTemplate retryTemplate = RetryTemplate.builder()
                .maxAttempts(1)
                .retryOn(TransientAiException.class)
                .fixedBackoff(Duration.ofSeconds(1))
                .withListener(new RetryListener() {

                    @Override
                    public <T extends Object, E extends Throwable> void onError(RetryContext context,
                                                                                RetryCallback<T, E> callback, Throwable throwable) {
                        log.warn("Retry error. Retry count:{}", context.getRetryCount(), throwable);
                    }
                })
                .build();
        return OllamaChatModel.builder()
                .ollamaApi(ollamaApi)
                .defaultOptions(OllamaOptions.builder().model(MODEL).temperature(0.9).build())
                .retryTemplate(retryTemplate)
                .build();
    }

}