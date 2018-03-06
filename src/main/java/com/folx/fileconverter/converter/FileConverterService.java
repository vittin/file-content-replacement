package com.folx.fileconverter.converter;

import org.springframework.stereotype.Service;

import java.nio.file.Path;

public interface FileConverterService {
    /**
     *
     * @param path Path to directory contains file or files which will be scanned for replacement.
     * @param fileExtension The suffix of the file(s)
     * @param from The sequence of bytes which will be replaced
     * @param to The sequence of bytes which will be appended
     */
    void replaceText(Path path, String fileExtension, byte[] from, byte[] to);
}
