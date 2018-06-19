package com.lvnluttngws.document.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

@Document(indexName = "lvnluttngidx", type = "judgmenttype")
/*@Setting(settingPath = "/es_entity_setting/judgment_setting.json")*/
/*@Mapping(mappingPath = "/es_entity_mapping/judgment_mapping.json")*/
public class JudgmentEs {
    @Id
    private String id;

    @Field(type = FieldType.String, store = true, analyzer = "vietnamese-analyzer")
    private String fileName;

    @Field(type = FieldType.String, store = true, analyzer = "vietnamese-analyzer")
    private String content;

    public JudgmentEs() {
    }

    public JudgmentEs(String id, String fileName, String content) {
        this.id = id;
        this.fileName = fileName;
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
