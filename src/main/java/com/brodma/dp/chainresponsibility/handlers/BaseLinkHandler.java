package com.brodma.dp.chainresponsibility.handlers;

import com.brodma.dp.chainresponsibility.client.FileRequest;
import com.brodma.dp.chainresponsibility.processors.FileProcessor;

public class BaseLinkHandler implements LinkHandler {

    private FileProcessor fileProcessor;

    public BaseLinkHandler(FileProcessor fileProcessor) {
        this.fileProcessor = fileProcessor;
    }

    @Override
    public void handle(FileRequest fileRequest, ChainFileHandler chain) {
        if (fileProcessor.hasMediaType(fileRequest)) {
            fileProcessor.process(fileRequest);
        } else {
            chain.doProcess(fileRequest);
        }
    }
}
