package com.brodma.dp.chainresponsibility.processors;

import com.brodma.dp.chainresponsibility.client.FileRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tika.exception.TikaException;
import org.apache.tika.io.TikaInputStream;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MediaType;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.html.HtmlParser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.SAXException;
import java.io.IOException;
import java.io.InputStream;


public class HtmlFileProcessor implements FileProcessor {

    private static final Logger LOG = LogManager.getLogger(HtmlFileProcessor.class);

    private HtmlParser htmlParser = new HtmlParser();

    @Override
    public void process(FileRequest fileRequest) {
        LOG.info("Processing HTML file type.");
        try (InputStream is = TikaInputStream.get(fileRequest.getFilePath())) {
            htmlParser.parse(is, new BodyContentHandler(), new Metadata(), new ParseContext());
        } catch (TikaException | SAXException | IOException e) {
            LOG.error(e);
        }
    }

    @Override
    public boolean hasMediaType(FileRequest fileRequest) {
        return fileRequest.getMediaType().equals(MediaType.TEXT_HTML);
    }
}
