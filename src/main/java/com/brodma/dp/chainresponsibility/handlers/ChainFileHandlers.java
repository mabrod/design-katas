package com.brodma.dp.chainresponsibility.handlers;

import com.brodma.dp.chainresponsibility.client.FileRequest;
import java.util.*;

public class ChainFileHandlers implements ChainFileHandler {

    private final Deque<LinkHandler> fileHandlers;

    public ChainFileHandlers(List<LinkHandler> handlers) {
        Objects.requireNonNull(handlers, "Collection of chain elements can not be null.");
        fileHandlers = new ArrayDeque<>(handlers);
    }

    @Override
    public void doProcess(FileRequest fileRequest) {
        if (!fileHandlers.isEmpty()) {
            LinkHandler handler = fileHandlers.pop();
            handler.handle(fileRequest, this);
        }
    }
}
