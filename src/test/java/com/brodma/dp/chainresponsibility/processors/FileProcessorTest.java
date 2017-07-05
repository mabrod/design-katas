package com.brodma.dp.chainresponsibility.processors;

import com.brodma.dp.chainresponsibility.client.FileRequest;
import org.apache.tika.mime.MediaType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static junit.framework.TestCase.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FileProcessorTest {

    @Mock
    private FileRequest fileRequest;

    @Test
    public void shouldMatchTextMediaType() {
        when(fileRequest.getMediaType()).thenReturn(MediaType.TEXT_PLAIN);
        TextFileProcessor text = new TextFileProcessor();
        assertTrue(text.hasMediaType(fileRequest));
    }

    @Test
    public void shouldMatchPdfMediaType() {
        when(fileRequest.getMediaType()).thenReturn(MediaType.application("pdf"));
        PDFFileProcessor pdf = new PDFFileProcessor();
        assertTrue(pdf.hasMediaType(fileRequest));
    }

    @Test
    public void shouldMatchHtmlfMediaType() {
        when(fileRequest.getMediaType()).thenReturn(MediaType.TEXT_HTML);
        HtmlFileProcessor html = new HtmlFileProcessor();
        assertTrue(html.hasMediaType(fileRequest));
    }
}