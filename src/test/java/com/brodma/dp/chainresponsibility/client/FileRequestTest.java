package com.brodma.dp.chainresponsibility.client;

import org.apache.tika.mime.MediaType;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import static org.assertj.core.api.Assertions.assertThat;

public class FileRequestTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    private FileRequest sut;


    @Test
    public void shouldThrowExceptionWhenPathIsNull() {
        exception.expect(IllegalArgumentException.class);
        sut = new FileRequest(null);
    }

    @Test
    public void shouldThrowExceptionWhenPathIsDirectory() throws Exception {
        exception.expect(IllegalArgumentException.class);
        File tmpDir = folder.newFolder("tmpDirectory");
        sut = new FileRequest(tmpDir.toPath());
    }

    @Test
    public void shouldThrowExceptionWhenFileIsNotReadable() throws Exception {
        exception.expect(IllegalArgumentException.class);
        File textFile = folder.newFile("someFile.txt");
        textFile.setReadable(false);
        Path path = textFile.toPath();
        sut = new FileRequest(path);
    }

    @Test
    public void shouldCreateFileRequestWithExistingReadableTextFile() throws Exception {
        File textFile = folder.newFile("textTmp.txt");
        textFile.setReadable(true);
        Files.write(textFile.toPath(), "content".getBytes("UTF8"));
        sut = new FileRequest(textFile.toPath());
        assertThat(sut).isNotNull();
        assertThat(sut.getMediaType()).isEqualTo(MediaType.TEXT_PLAIN);
        assertThat(sut.getFilePath()).isEqualTo(textFile.toPath());
    }

    @Test
    public void shouldCreateFileRequestWithExistingReadableHtmlFile() throws Exception {
        File htmlFile = folder.newFile("webTmp.html");
        htmlFile.setReadable(true);
        Files.write(htmlFile.toPath(), "<html><body><p>some paragrahp</p></body></html>".getBytes("UTF8"));
        sut = new FileRequest(htmlFile.toPath());
        assertThat(sut).isNotNull();
        assertThat(sut.getMediaType()).isEqualTo(MediaType.TEXT_HTML);
        assertThat(sut.getFilePath()).isEqualTo(htmlFile.toPath());
    }
}