package com.lvnluttngws.document.helper;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.PDFTextStripperByArea;

import java.io.File;
import java.io.IOException;

/*Extract text from a large pdf*/
public class PDFToText {

    public static String parse(File file) {
        PDDocument document = null;
        try {
            document = PDDocument.load(file);
            document.getClass();

            if (!document.isEncrypted()) {

                PDFTextStripperByArea stripper = new PDFTextStripperByArea();
                stripper.setSortByPosition(true);

                PDFTextStripper tStripper = new PDFTextStripper();

                String pdfFileInText = tStripper.getText(document);
                // remove empty lines
                return pdfFileInText.replaceAll("(?m)^[ \t]*\r?\n", "");
            }

        } catch (Exception e) {
            System.out.println(e.getCause());
        } finally {
            if(document != null) {
                try {
                    document.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}