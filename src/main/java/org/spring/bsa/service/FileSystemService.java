package org.spring.bsa.service;

import lombok.Getter;
import org.spring.bsa.entities.GifEntity;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.time.LocalDate;

@Service
@Getter
public class FileSystemService {

	private final String PATH = "D:\\BSA\\Spring Boot\\";

	public File downloadGif(GifEntity inputGifEntity) {
		try (var inputStream = new BufferedInputStream(new URL(inputGifEntity.getUrl()).openStream())) {
			File directory = new File(PATH + "cache\\" + inputGifEntity.getQuery());
			if (!directory.exists()) {
				directory.mkdir();
			}

			File gif = new File(directory, inputGifEntity.getId() + ".gif");
			var fileOutputStream = new FileOutputStream(gif);
			byte[] buffer = new byte[2048];
			int inputByte;

			while ((inputByte = inputStream.read(buffer, 0, 2048)) != -1) {
				fileOutputStream.write(buffer, 0, inputByte);
			}
			return gif;
		}
		catch (Exception exception) {
			exception.printStackTrace();
		}

		return null;
	}

	public void addToUserFolder(String userId, GifEntity gifEntity) {
		String path = PATH + "\\users\\" + userId + "\\" + gifEntity.getQuery() + "\\";

		File directory = new File(path);

		if (!directory.exists()) {
			directory.mkdirs();
		}

		File cacheGif = getGifPath(gifEntity);

		if (cacheGif != null) {
			cacheGif = new File(cacheGif.getAbsolutePath());
		}
		else {
			cacheGif = downloadGif(gifEntity);
		}

		copyGifToUserFolder(userId, gifEntity.getQuery(), cacheGif.getPath());
	}

	private File copyGifToUserFolder(String userId, String query, String cachePath) {
		File source = new File(cachePath);
		File destination = new File(PATH + "\\users\\" + userId + "\\" + query);

		try {
			destination.mkdirs();
			destination = new File(destination, source.getName());
			Files.copy(source.toPath(), destination.toPath());
			historyWriter(userId, query, destination);
		}
		catch (IOException exception) {
			System.out.println("File exist");
		}

		return new File(destination, source.getName());
	}

	private File getGifPath(GifEntity gifEntity) {
		File gif = new File(PATH + "cache\\" + gifEntity.getQuery(), gifEntity.getId() + ".gif");
		return gif.exists() ? gif : null;
	}

	private void historyWriter(String userId, String query, File saveFile) {
		File historyUserFile = new File(PATH + "\\users" + "\\" + userId, "history.csv");

		try (PrintStream printStream = new PrintStream(new FileOutputStream(historyUserFile, true))) {
			if (!historyUserFile.exists()) {
				historyUserFile.createNewFile();
			}

			String history = LocalDate.now().toString() + "," + query + "," + saveFile.getAbsolutePath();
			printStream.println(history);

		}
		catch (IOException exception) {
			exception.printStackTrace();
		}
	}

	public File getHistory(String userId) {
		return new File(PATH + "users\\" + userId + "\\history.csv");
	}

	public boolean deleteHistory(String userId) {
		return new File(PATH + "users\\" + userId + "\\history.csv").delete();
	}

}
