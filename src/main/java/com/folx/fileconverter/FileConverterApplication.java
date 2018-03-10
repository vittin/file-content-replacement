package com.folx.fileconverter;

import com.folx.fileconverter.converter.FileConverterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.nio.file.Path;

/**
 * Spring is not required - just remove the annotation {@link org.springframework.stereotype.Service}
 * at {@link com.folx.fileconverter.converter.FileConverterServiceImpl} and remove this class.
 * Then, write main method and create reference to {@link com.folx.fileconverter.converter.FileConverterService}
 *
 * {@see {@link com.folx.fileconverter.converter.FileConverterService#replaceContent(Path, String, byte[], byte[])}}
 */
@SpringBootApplication
public class FileConverterApplication implements CommandLineRunner {

	private FileConverterService fileConverterService;
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	public FileConverterApplication(FileConverterService fileConverterService) {
		this.fileConverterService = fileConverterService;
	}

	public static void main(String[] args) {
		SpringApplication.run(FileConverterApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		if (args.length == 4) {
			Path path = new File(args[0]).toPath();
			String fileExtension = args[1].replace(".", "");
			byte fromBytes[] = args[2].getBytes();
			byte toBytes[] = args[3].getBytes();
			fileConverterService.replaceContent(path, fileExtension, fromBytes, toBytes);
		} else {
			logger.error("You should provide exactly 4 arguments! " + "\n" +
					"1) Path" + "\n" +
					"2) Files extension" + "\n" +
					"3) Bytes pattern which should be replaced" + "\n" +
					"4) Bytes pattern which should be appended as replacement");
		}
	}
}
