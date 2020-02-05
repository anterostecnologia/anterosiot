package br.com.anteros.iot.support.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import org.apache.commons.io.FileUtils;

public class StaticUtil {
	private static final BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
	public static String userInput(String prompt) {
		String retString = "";
		System.err.print(prompt);
		try {
			retString = stdin.readLine();
		} catch (Exception e) {
			System.out.println(e);
			try {
				userInput("<Oooch/>");
			} catch (Exception exception) {
				exception.printStackTrace();
			}
		}
		return retString;
	}

	public static byte[] appendByteArrays(byte c[], byte b[], int n) {
		int newLength = c != null ? c.length + n : n;
		byte newContent[] = new byte[newLength];
		if (c != null) {
			for (int i = 0; i < c.length; i++)
				newContent[i] = c[i];
		}
		int offset = (c != null ? c.length : 0);
		for (int i = 0; i < n; i++)
			newContent[offset + i] = b[i];
		return newContent;
	}

	public static void main(String... args) {
		String akeu = userInput("Tell me > ");
		System.out.println(akeu);
	}
	
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
		targetFile.createNewFile();
	    FileUtils.copyInputStreamToFile(stream, targetFile);
	    
	    return targetFile;
	}

}
