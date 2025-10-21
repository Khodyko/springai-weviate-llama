package org.khodyko.docragai.advisor;

import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.AdvisorChain;
import org.springframework.ai.chat.client.advisor.api.BaseAdvisor;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.filter.Filter;
import org.springframework.ai.vectorstore.filter.FilterExpressionTextParser;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import reactor.core.scheduler.Scheduler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DocumentAdvisor implements BaseAdvisor {
    public static final String RETRIEVED_DOCUMENTS = "qa_retrieved_documents";

    public static final String FILTER_EXPRESSION = "qa_filter_expression";

    private static final PromptTemplate DEFAULT_PROMPT_TEMPLATE = new PromptTemplate("""
			{query}

			Context information is below, surrounded by ---------------------

			---------------------
			{question_answer_context}
			---------------------

			Given the context and provided history information and not prior knowledge,
			reply to the user comment. If the answer is not in the context, inform
			the user that you can't answer the question.
			""");

    private static final int DEFAULT_ORDER = 0;

    private final PromptTemplate promptTemplate;

    private final SearchRequest searchRequest;

    private final Scheduler scheduler;

    private final int order;

    List<Document> documents;

    public List<Document> getDocuments() {
        return documents;
    }

    public void setDocuments(List<Document> documents) {
        this.documents = documents;
    }

    public DocumentAdvisor() {
        this(SearchRequest.builder().build(), DEFAULT_PROMPT_TEMPLATE, BaseAdvisor.DEFAULT_SCHEDULER,
                DEFAULT_ORDER);
    }

    DocumentAdvisor(SearchRequest searchRequest, @Nullable PromptTemplate promptTemplate,
                    @Nullable Scheduler scheduler, int order) {
        Assert.notNull(searchRequest, "searchRequest cannot be null");

        this.searchRequest = searchRequest;
        this.promptTemplate = promptTemplate != null ? promptTemplate : DEFAULT_PROMPT_TEMPLATE;
        this.scheduler = scheduler != null ? scheduler : BaseAdvisor.DEFAULT_SCHEDULER;
        this.order = order;
    }

    @Override
    public ChatClientRequest before(ChatClientRequest chatClientRequest, AdvisorChain advisorChain) {
        // 1. Search for similar documents in the vector store.

        List<Document> documents = getDocuments();

        // 2. Create the context from the documents.
        Map<String, Object> context = new HashMap<>(chatClientRequest.context());
        context.put(RETRIEVED_DOCUMENTS, documents);

        String documentContext = documents == null ? ""
                : documents.stream().map(Document::getText).collect(Collectors.joining(System.lineSeparator()));

        // 3. Augment the user prompt with the document context.
        UserMessage userMessage = chatClientRequest.prompt().getUserMessage();
        String augmentedUserText = this.promptTemplate
                .render(Map.of("query", userMessage.getText(), "question_answer_context", documentContext));

        // 4. Update ChatClientRequest with augmented prompt.
        return chatClientRequest.mutate()
                .prompt(chatClientRequest.prompt().augmentUserMessage(augmentedUserText))
                .context(context)
                .build();
    }

    @Override
    public ChatClientResponse after(ChatClientResponse chatClientResponse, AdvisorChain advisorChain) {
        ChatResponse.Builder chatResponseBuilder;
        setDocuments(new ArrayList<>());
        if (chatClientResponse.chatResponse() == null) {
            chatResponseBuilder = ChatResponse.builder();
        }
        else {
            chatResponseBuilder = ChatResponse.builder().from(chatClientResponse.chatResponse());
        }
        chatResponseBuilder.metadata(RETRIEVED_DOCUMENTS, chatClientResponse.context().get(RETRIEVED_DOCUMENTS));
        return ChatClientResponse.builder()
                .chatResponse(chatResponseBuilder.build())
                .context(chatClientResponse.context())
                .build();
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
