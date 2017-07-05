package com.brodma.dp.chainresponsibility.handlers;

import com.brodma.dp.chainresponsibility.client.FileRequest;

public interface LinkHandler {

    void handle(FileRequest fileRequest, ChainFileHandler chainFileHandler);
}
