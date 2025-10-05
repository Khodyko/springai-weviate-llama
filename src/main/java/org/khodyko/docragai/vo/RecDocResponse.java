package org.khodyko.docragai.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;
import org.springframework.ai.document.Document;

import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldNameConstants
public class RecDocResponse {
    private String uid;
    private String textChunk;

    public RecDocResponse(Document document) {
        uid = Objects.toString(document.getMetadata().get(Fields.uid), null);
        textChunk = document.getText();
    }
}
