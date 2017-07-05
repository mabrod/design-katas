package com.brodma.dp.chainresponsibility.handlers;

import com.brodma.dp.chainresponsibility.client.FileRequest;

public interface ChainFileHandler {

    void doProcess(FileRequest fileRequest);
}
