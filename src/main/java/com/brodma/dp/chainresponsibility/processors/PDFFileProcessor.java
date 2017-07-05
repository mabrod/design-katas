package com.brodma.dp.chainresponsibility.processors;

import com.brodma.dp.chainresponsibility.client.FileRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.pdfbox.io.RandomAccessFile;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.tika.mime.MediaType;
import java.io.IOException;

public class PDFFileProcessor implements FileProcessor {

    private static final Logger LOG = LogManager.getLogger(PDFFileProcessor.class);

    private PDFParser pdfParser;

    @Override
    public void process(FileRequest fileRequest) {
        LOG.info("Processing PDF file type.");
        try(RandomAccessFile raf = new RandomAccessFile(fileRequest.toFile(), "rw")) {
            pdfParser = new PDFParser(raf);
            pdfParser.parse();
        } catch (IOException e) {
            LOG.error(e);
        }
    }

    @Override
    public boolean hasMediaType(FileRequest fileRequest) {
        return fileRequest.getMediaType().equals(MediaType.application("pdf"));
    }

}
