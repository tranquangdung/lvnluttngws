package com.lvnluttngws.document.service;

import java.io.File;
import java.util.List;

public interface FileService {
    List<File> getFileInFolder(String dir);

    boolean fileProcess(File file);

    void folderProcess(String folderPath);
}
