package com.folx.fileconverter.converter;

import java.nio.file.Path;

public interface FileConverterService {
    /**
     *
     * @param path The directory contains file or files which will be scanned for replacement.
     * @param fileExtension The suffix of the file(s)
     * @param from The sequence of bytes which will be replaced
     * @param to The sequence of bytes which will be appended
     */
    void replaceContent(Path path, String fileExtension, byte[] from, byte[] to);
}
