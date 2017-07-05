package com.brodma.dp.chainresponsibility;

import com.brodma.dp.chainresponsibility.client.FileRequest;
import com.brodma.dp.chainresponsibility.handlers.*;
import com.brodma.dp.chainresponsibility.processors.HtmlFileProcessor;
import com.brodma.dp.chainresponsibility.processors.PDFFileProcessor;
import com.brodma.dp.chainresponsibility.processors.TextFileProcessor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;
import static java.util.stream.Collectors.toList;

public class ChainResponsibilityRunner {

    private static final Logger LOG = LogManager.getLogger(ChainResponsibilityRunner.class);

    public static void main(String[] args) throws Exception {

        if (args.length != 1) {
            LOG.info("Provide path to a file to be processed.");
            LOG.info("e.g java -jar <your_jar> test.txt");
            return;
        }

        FileRequest fileRequest = new FileRequest(Paths.get(args[0]));
        LinkHandler txt = new BaseLinkHandler(new TextFileProcessor());
        LinkHandler pdf = new BaseLinkHandler(new PDFFileProcessor());
        LinkHandler html = new BaseLinkHandler(new HtmlFileProcessor());
        List<LinkHandler> fileHandlers = Stream.of(txt,pdf,html).collect(toList());
        ChainFileHandler chain = new ChainFileHandlers(fileHandlers);
        chain.doProcess(fileRequest);
    }
}
