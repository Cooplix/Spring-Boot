package org.spring.bsa.service;

import org.spring.bsa.entities.GifEntity;
import org.springframework.stereotype.Service;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;

@Service
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

		if(!directory.exists()) {
			directory.mkdirs();
		}
		
		File cacheGif = getGifPath(gifEntity);
		
		if(cacheGif != null) {
			cacheGif = new File(cacheGif.getAbsolutePath());
		} else {
			cacheGif = downloadGif(gifEntity);
		}

		copyGifToUserFolder(userId, gifEntity.getQuery(), cacheGif.getPath());
	}

	private void copyGifToUserFolder(String userId, String query, String path) {
	}

	private File getGifPath(GifEntity gifEntity) {
	}

}
