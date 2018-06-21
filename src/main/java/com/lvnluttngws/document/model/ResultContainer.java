package com.lvnluttngws.document.model;

import java.util.List;

public class ResultContainer {
    private List<Result> result;

    private Long totalHits;

    public Long getTotalHits() {
        return totalHits;
    }

    public void setTotalHits(Long totalHits) {
        this.totalHits = totalHits;
    }

    public List<Result> getResult() {
        return result;
    }

    public void setResult(List<Result> result) {
        this.result = result;
    }
}
