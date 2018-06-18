package com.lvnluttngws.document.service.impl;

import com.lvnluttngws.document.common.Category;
import com.lvnluttngws.document.helper.PDFToText;
import com.lvnluttngws.document.model.Judgment;
import com.lvnluttngws.document.service.FileService;
import com.lvnluttngws.document.service.JudgmentService;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.File;
import java.util.Date;
import java.util.List;

@Service
public class FileServiceImpl implements FileService {
    private static final Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

    @Autowired
    private JudgmentService judgmentService;

    @Override
    public List<File> getFileInFolder(String directory) {
        if(StringUtils.isEmpty(directory)) {
            return null;
        }
        File dir = new File(directory);
        String[] extensions = new String[] { "pdf", "PDF" };
        return (List<File>) FileUtils.listFiles(dir, extensions, true);
    }

    @Override
    public boolean fileProcess(File file) {
        Date date = new Date();
        Judgment judgment = new Judgment();
        judgment.setUserId(1);
        judgment.setCategoryId(Category.HS.getInt());
        judgment.setCreatedDate(date);
        judgment.setUpdatedDate(date);
        judgment.setFileName(file.getName());
        judgment.setFilePath(file.getAbsolutePath());
        judgment.setContent(PDFToText.parse(file));
        Judgment jud = judgmentService.addRecord(judgment);
        if (jud.getId() != null) {
            return true;
        }

        return false;
    }

    @Override
    public void folderProcess(String folderPath) {
        List<File> files = getFileInFolder(folderPath);
        if (!CollectionUtils.isEmpty(files)) {
            for (final File file : files) {
                boolean success = fileProcess(file);
                if (!success) {
                    logger.error("Get text from file error: " + file.getAbsolutePath());
                }
            }
        }
    }
}
