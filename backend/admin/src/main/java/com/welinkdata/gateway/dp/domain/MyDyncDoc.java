package com.welinkdata.gateway.dp.domain;

import com.welinkdata.gateway.common.core.domain.MongoEntity;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashMap;

@Data
@ToString
@Document(collection = "bizdoc_dync_doc")
public class MyDyncDoc extends MongoEntity {

    @Id
    private String id;

    private String docType;

    private HashMap f = new HashMap();
}

