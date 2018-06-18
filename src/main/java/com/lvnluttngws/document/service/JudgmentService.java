package com.lvnluttngws.document.service;

import com.lvnluttngws.document.model.Judgment;
import com.lvnluttngws.document.model.ResultContainer;
import com.lvnluttngws.document.model.SearchInput;

public interface JudgmentService {
    void reindexDB();

    Judgment addRecord(Judgment judgment);

    ResultContainer search(SearchInput searchInput);
}
