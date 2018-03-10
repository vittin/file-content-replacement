package com.folx.fileconverter.converter;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class FileConverterServiceImplTest {

    private static Path filesDirPath;
    private static Map<String, File> expectedFiles;
    private Map<String, File> returnedFiles;
    private FileConverterService fileConverterService;

    @BeforeClass
    public static void setUpClass() throws Exception {
        FileUtils.copyDirectory(new ClassPathResource("/ro").getFile(), new ClassPathResource("/rw").getFile());
        expectedFiles = getFiles("/expected");
        filesDirPath = new ClassPathResource("/rw").getFile().toPath();
    }

    @Before
    public void setUp() throws Exception {
        fileConverterService = new FileConverterServiceImpl();
    }

    @Test
    public void given_extension_when_convert_shouldConvertFilesWithExtension() throws Exception {

        fileConverterService.replaceContent(filesDirPath, "txt", "Lorem ipsum".getBytes(), "Folx rulez".getBytes());
        returnedFiles = getFiles("/rw");

        assertThat(bytesOf(returnedFiles.get("lorem.txt")), is(not(nullValue())));
        assertThat(bytesOf(returnedFiles.get("lorem.txt")), is(equalTo(bytesOf(expectedFiles.get("lorem.txt")))));


    }

    @Test
    public void given_extension_when_convert_shouldNotConvertFilesWithDifferentExtension() {

        fileConverterService.replaceContent(filesDirPath, "txt", "Lorem ipsum".getBytes(), "Folx rulez".getBytes());
        returnedFiles = getFiles("/rw");

        assertThat(bytesOf(returnedFiles.get("lorem.not-txt")), is(not(nullValue())));
        assertThat(bytesOf(returnedFiles.get("lorem.not-txt")), is(equalTo(bytesOf(expectedFiles.get("lorem.not-txt")))));
    }

    @Test
    public void given_path_when_convert_shouldRecursiveChangeAllFiles() {
        fileConverterService.replaceContent(filesDirPath, "txt", "Lorem ipsum".getBytes(), "Folx rulez".getBytes());
        returnedFiles = getFiles("/rw");

        assertThat(bytesOf(returnedFiles.get("also-lorem.txt")), is(not(nullValue())));
        assertThat(bytesOf(returnedFiles.get("also-lorem.txt")), is(equalTo(bytesOf(expectedFiles.get("also-lorem.txt")))));
        assertThat(bytesOf(returnedFiles.get("lorem-inside.txt")), is(not(nullValue())));
        assertThat(bytesOf(returnedFiles.get("lorem-inside.txt")), is((equalTo(bytesOf(expectedFiles.get("lorem-inside.txt"))))));

    }

    @Test
    public void given_subdirs_when_convert_should_CheckAllFiles() {
        fileConverterService.replaceContent(filesDirPath, "txt", "Lorem ipsum".getBytes(), "Folx rulez".getBytes());
        returnedFiles = getFiles("/rw");

        assertThat(returnedFiles.keySet(), hasSize(6));
    }

    @Test
    public void shouldConvertBinaryFilesToo() {
        fileConverterService.replaceContent(filesDirPath, "png", "�\u0003�x���E�lKD�".getBytes(), "Folx rulez".getBytes());

        returnedFiles = getFiles("/rw");

        assertThat(returnedFiles.keySet(), is(not(empty())));
        assertThat(returnedFiles.get("lorem.png"), is(not(nullValue())));
        assertThat(new String(bytesOf(returnedFiles.get("lorem.png"))), containsString("Folx rulez"));
    }

    private static Map<String, File> getFiles(String path) {
        Map<String, File> files = new HashMap<>();
        Arrays.asList(path + "/lorem.txt",
                path + "/lorem.not-txt",
                path + "/subdir/also-lorem.txt",
                path + "/subdir/not-lorem.txt",
                path + "/subdir/another-subdir/lorem.png",
                path + "/subdir/another-subdir/lorem-inside.txt").forEach(p -> getFile(p).ifPresent(
                f -> files.put(f.getName(), f)
        ));
        return files;
    }

    private static Optional<File> getFile(String path){
        try {
            File f = new ClassPathResource(path).getFile();
            return Optional.of(f);
        } catch (IOException ex){
            return Optional.empty();
        }
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
}