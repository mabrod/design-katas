package com.brodma.dp.chainresponsibility.handlers;

import com.brodma.dp.chainresponsibility.client.FileRequest;
import com.brodma.dp.chainresponsibility.processors.HtmlFileProcessor;
import com.brodma.dp.chainresponsibility.processors.PDFFileProcessor;
import com.brodma.dp.chainresponsibility.processors.TextFileProcessor;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;
import static java.util.stream.Collectors.toList;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ChainFileHandlersTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    private ChainFileHandler sut;

    @Mock
    private FileRequest fileRequest;

    @Mock
    private TextFileProcessor textFileProcessor;

    @Mock
    private PDFFileProcessor pdfFileProcessor;

    @Mock
    private HtmlFileProcessor htmlFileProcessor;

    @Before
    public void setUp() {
        LinkHandler txt = new BaseLinkHandler(textFileProcessor);
        LinkHandler pdf = new BaseLinkHandler(pdfFileProcessor);
        LinkHandler html = new BaseLinkHandler(htmlFileProcessor);
        List<LinkHandler> fileHandlers = Stream.of(txt,pdf,html).collect(toList());
        sut = new ChainFileHandlers(fileHandlers);
    }

    @Test
    public void shouldProcessOnlyTEXTFile() {
        when(textFileProcessor.hasMediaType(fileRequest)).thenReturn(true);
        sut.doProcess(fileRequest);
        verify(textFileProcessor).process(fileRequest);
        verify(pdfFileProcessor, never()).process(fileRequest);
        verify(htmlFileProcessor, never()).process(fileRequest);
    }

    @Test
    public void shouldProcessOnlyPDFFile() {
        when(pdfFileProcessor.hasMediaType(fileRequest)).thenReturn(true);
        sut.doProcess(fileRequest);
        verify(pdfFileProcessor).process(fileRequest);
        verify(textFileProcessor, never()).process(fileRequest);
        verify(htmlFileProcessor, never()).process(fileRequest);
    }

    @Test
    public void shouldProcessOnlyHTMLFile() {
        when(htmlFileProcessor.hasMediaType(fileRequest)).thenReturn(true);
        sut.doProcess(fileRequest);
        verify(htmlFileProcessor).process(fileRequest);
        verify(textFileProcessor, never()).process(fileRequest);
        verify(pdfFileProcessor, never()).process(fileRequest);
    }

    @Test
    public void shouldNotProcessAnyFilesWhenChainIsEmpty() {
        sut = new ChainFileHandlers(Collections.emptyList());
        sut.doProcess(fileRequest);
        verify(htmlFileProcessor, never()).process(fileRequest);
        verify(textFileProcessor, never()).process(fileRequest);
        verify(pdfFileProcessor, never()).process(fileRequest);
    }

    @Test
    public void shouldThrowExceptionWhenBuildingChainWithNullHandlersReference() {
        exception.expect(NullPointerException.class);
        sut = new ChainFileHandlers(null);
    }
}