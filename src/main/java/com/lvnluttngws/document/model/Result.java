package com.lvnluttngws.document.model;

import java.util.List;

public class Result {

    private String id;
    private List<String> highLight;
    private String filePath;
    private Float score;

    public String getId() {
        return id;
    }

    public Float getScore() {
        return score;
    }

    public void setScore(Float score) {
        this.score = score;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getHighLight() {
        return highLight;
    }

    public void setHighLight(List<String> highLight) {
        this.highLight = highLight;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
