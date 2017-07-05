package com.brodma.dp.chainresponsibility.client;

import org.apache.tika.config.TikaConfig;
import org.apache.tika.io.TikaInputStream;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MediaType;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

public class FileRequest implements Serializable {

    private Path filePath;
    private MediaType mediaType;

    public FileRequest(Path filePath) {
        validate(filePath);
        this.filePath = filePath;
        try (InputStream is = TikaInputStream.get(filePath)) {
            mediaType = TikaConfig.getDefaultConfig().getDetector().detect(TikaInputStream.get(is), new Metadata());
        } catch (IOException ioe) {
            throw new IllegalStateException(ioe);
        }
    }

    public Path getFilePath() {
        return filePath;
    }

    public File toFile() {
        return getFilePath().toFile();
    }

    public MediaType getMediaType() {
        return mediaType;
    }

    public void validate(Path filePath) {

        if (Objects.isNull(filePath)) {
            throw new IllegalArgumentException("path to file can not be null.");
        }

        if (!Files.exists(filePath)) {
            throw new IllegalArgumentException("file does not exist.");
        }

        if (Files.isDirectory(filePath)) {
            throw new IllegalArgumentException("file can not be a directory.");
        }

        if (!Files.isReadable(filePath)) {
            throw new IllegalArgumentException("file must be readable.");
        }
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("FileRequest{");
        sb.append("file=").append(filePath);
        sb.append(", mediaType=").append(mediaType);
        sb.append('}');
        return sb.toString();
    }
}
