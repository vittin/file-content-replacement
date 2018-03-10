package com.folx.fileconverter.converter;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

@Service
public class FileConverterServiceImpl implements FileConverterService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void replaceContent(Path path, String fileExtension, byte[] from, byte[] to) {
        try {
            Files.walk(path)
                    .filter(f -> Objects.equals(fileExtension, getFileExtension(f.toFile())))
                    .map(f -> new Pair(f.toAbsolutePath(), bytesOf(f.toFile())))
                    .forEach(p -> p.getFileContent().ifPresent(c ->
                            writeTo(p.getFilePath(), replaceBytes(c, from, to))));
        } catch (IOException ex){
            logger.warn(ex.getMessage(), ex);
        }
    }

    private void writeTo(Path filePath, byte[] bytes) {
        try {
            FileUtils.writeByteArrayToFile(filePath.toFile(), bytes);
            logger.info("Replaced file {}", filePath);
        } catch (IOException ex) {
            logger.warn("Failed to replace {}", filePath);
            logger.warn(ex.toString(), ex);
        }
    }

    private byte[] replaceBytes(byte[] fileContent, byte[] from, byte[] to) {
        return new String(fileContent).replaceAll(new String(from), new String(to)).getBytes();
    }

    private byte[] bytesOf(File file){
        if (file == null){
            return null;
        }
        try {
            return Files.readAllBytes(file.toPath());
        } catch (IOException ex) {
            return null;
        }
    }

    //yeah, method from apache commons
    private String getFileExtension(File file) {
        String fileName = file.getName();
        if (fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0) {
            return fileName.substring(fileName.lastIndexOf(".")+1);
        }
        return "";
    }
}
