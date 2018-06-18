package com.lvnluttngws.document.helper;

import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

import java.io.*;

/*Extract text from a large pdf*/
public class PDFToText {

    public static String parse(File file) {
        InputStream is = null;
        try {
            is = new BufferedInputStream(new FileInputStream(file));

            Parser parser = new AutoDetectParser();
            ContentHandler handler = new BodyContentHandler(Integer.MAX_VALUE);

            Metadata metadata = new Metadata();

            parser.parse(is, handler, metadata, new ParseContext());

            return handler.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TikaException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}