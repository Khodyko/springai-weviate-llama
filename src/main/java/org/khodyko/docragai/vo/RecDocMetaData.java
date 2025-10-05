package org.khodyko.docragai.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

import java.util.HashMap;
import java.util.Map;

/**
 * Fields of this entity should be the same as
 * vectorstore.weaviate.filter-field
 * in application.yml
 */
@Getter
@Setter
@FieldNameConstants
public class RecDocMetaData {

    private String uid;

    public Map<String, Object> getMetaDataMap() {
        Map<String, Object> map = new HashMap<>();
        map.put(Fields.uid, uid);
        return map;
    }
}
