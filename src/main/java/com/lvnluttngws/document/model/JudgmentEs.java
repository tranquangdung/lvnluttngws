package com.lvnluttngws.document.model;

import com.lvnluttngws.document.common.ESConstant;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.io.Serializable;

@Document(indexName = ESConstant.ES_INDEX_NAME, type = ESConstant.ES_TYPE)
/*@Setting(settingPath = "/settings/lawonline-settings.json")
@Mapping(mappingPath = "/mappings/lawonline-mappings.json")*/
public class JudgmentEs implements Serializable {
    @Id
    private String id;

    private String fileName;

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
