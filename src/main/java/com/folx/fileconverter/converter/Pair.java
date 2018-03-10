package com.folx.fileconverter.converter;

import java.nio.file.Path;
import java.util.Optional;

public class Pair {

    private final Path filePath;
    private final byte[] fileContent;

    Pair(Path fileName, byte[] fileContent) {
        this.filePath = fileName;
        this.fileContent = fileContent;
    }

    public Path getFilePath() {
        return filePath;
    }

    public Optional<byte[]> getFileContent() {
        return Optional.ofNullable(fileContent);
    }
}
