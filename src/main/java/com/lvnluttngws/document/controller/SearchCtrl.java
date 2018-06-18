package com.lvnluttngws.document.controller;

import com.lvnluttngws.document.model.AddInput;
import com.lvnluttngws.document.model.ResultContainer;
import com.lvnluttngws.document.model.SearchInput;
import com.lvnluttngws.document.service.FileService;
import com.lvnluttngws.document.service.JudgmentService;
import org.elasticsearch.common.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.File;

@RestController
@RequestMapping("/api")
public class SearchCtrl {
    private static Logger logger = LoggerFactory.getLogger(SearchCtrl.class);

    @Autowired
    private JudgmentService judgmentService;

    @Autowired
    private FileService fileService;

    @PostMapping("/importdoc")
    public String importDocument(@Valid @RequestBody AddInput addInput) {
        if (addInput == null || Strings.isEmpty(addInput.getUrl())) {
            return "FAIL";
        }

        fileService.folderProcess(addInput.getUrl());

        return "OK";
    }

    // add document
    @PostMapping("/adddoc")
    public String createDocument(@Valid @RequestBody AddInput addInput) {
        if (addInput == null || Strings.isEmpty(addInput.getUrl())) {
            logger.debug("Missing file url");
        } else {
            boolean result = fileService.fileProcess(new File(addInput.getUrl()));
            if (result) {
                return "OK";
            }
        }

        return "FAIL";
    }

    @PostMapping("/search")
    public ResultContainer search(@Valid @RequestBody SearchInput searchInput) {
        return judgmentService.search(searchInput);
    }
}
