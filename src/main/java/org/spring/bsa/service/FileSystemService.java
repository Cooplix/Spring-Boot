package org.spring.bsa.service;

import org.spring.bsa.entities.GifEntity;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;

public class FileSystemService {

	private final String PATH = ".\\";

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

}
