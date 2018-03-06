package com.folx.fileconverter.converter;

import org.springframework.stereotype.Service;

import java.nio.file.Path;

@Service
public class FileConverterServiceImpl implements FileConverterService {
    @Override
    public void replaceText(Path path, String fileExtension, byte[] from, byte[] to) {

    }
}
