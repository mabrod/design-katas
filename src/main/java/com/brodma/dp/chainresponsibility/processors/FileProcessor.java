package com.brodma.dp.chainresponsibility.processors;

import com.brodma.dp.chainresponsibility.client.FileRequest;

public interface FileProcessor {

    void process(FileRequest fileRequest);

    boolean hasMediaType(FileRequest fileRequest);
}
