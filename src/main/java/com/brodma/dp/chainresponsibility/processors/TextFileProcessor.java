package com.brodma.dp.chainresponsibility.processors;

import com.brodma.dp.chainresponsibility.client.FileRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tika.exception.TikaException;
import org.apache.tika.io.TikaInputStream;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MediaType;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.txt.TXTParser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.SAXException;
import java.io.*;

public class TextFileProcessor implements FileProcessor {

    private static final Logger LOG = LogManager.getLogger(TextFileProcessor.class);

    private TXTParser txtParser;

    public TextFileProcessor() {
        txtParser = new TXTParser();
    }

    @Override
    public void process(FileRequest fileRequest) {
        LOG.info("Processing txt file type.");
        try(InputStream is = TikaInputStream.get(fileRequest.getFilePath())) {
            BodyContentHandler contentHandler = new BodyContentHandler( );
            txtParser.parse(is, contentHandler, new Metadata(), new ParseContext());
        } catch (SAXException | TikaException | IOException e) {
            LOG.error(e);
        }
    }

    @Override
    public boolean hasMediaType(FileRequest fileRequest) {
        return fileRequest.getMediaType().equals(MediaType.TEXT_PLAIN);
    }

}
