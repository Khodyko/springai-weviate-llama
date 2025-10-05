package org.khodyko.docragai.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.ai.document.Document;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class AiRecDocResponse {
    private List<RecDocResponse> recDocResponseList;
    private String aiResponse;

    public AiRecDocResponse(List<Document> documents, String aiResponse) {
        this.recDocResponseList = new ArrayList<>();

        if (CollectionUtils.isEmpty(documents)) {
            this.aiResponse = aiResponse;
            //todo delete and uncomment
//            this.aiResponse = "Записи не найдены.";
        } else {
            this.aiResponse = aiResponse;
            for (Document document : documents) {
                recDocResponseList.add(new RecDocResponse(document));
            }
        }
    }
}
