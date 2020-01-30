package br.com.anteros.iot.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import org.apache.commons.io.FileUtils;

public class Utils {

	public static String readLineByLineOfFile(String filePath) {
		StringBuilder contentBuilder = new StringBuilder();

		try (Stream<String> stream = Files.lines(Paths.get(filePath), StandardCharsets.UTF_8)) {
			stream.forEach(s -> contentBuilder.append(s).append("\n"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		return contentBuilder.toString();
	}
	
	public static File inputStreamToFile(InputStream stream, String path) throws IOException {
		
		File targetFile = new File(path);
	    FileUtils.copyInputStreamToFile(stream, targetFile);
	    
	    return targetFile;
	}

}
