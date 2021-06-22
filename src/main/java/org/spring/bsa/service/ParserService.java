package org.spring.bsa.service;

import org.spring.bsa.dto.CacheDto;
import org.spring.bsa.dto.HistoryDto;
import org.springframework.stereotype.Service;

import java.io.*;
import java.time.LocalDate;
import java.util.*;

@Service
public class ParserService {

	public CacheDto[] parseCache(Map<String, File[]> queriedCache) {

		Collection<CacheDto> cache = new ArrayList<>();

		for (Map.Entry<String, File[]> entry : queriedCache.entrySet()) {
			Collection<String> paths = new ArrayList<>();
			Queue<File> filesToCheck = new PriorityQueue<>(Arrays.asList(entry.getValue()));

			while (!filesToCheck.isEmpty()) {
				File file = filesToCheck.poll();
				if (file.isDirectory()) {
					Collections.addAll(filesToCheck, file.listFiles());
				}
				else {
					paths.add(file.getAbsolutePath());
				}
			}

			CacheDto dto = new CacheDto();
			dto.setQuery(entry.getKey());
			dto.setGifs(paths.toArray(new String[0]));

			cache.add(dto);
		}

		return cache.toArray(new CacheDto[0]);
	}

	public String[] parseOnlyFiles(Map<String, File[]> queriedCache) {
		var cacheDto = parseCache(queriedCache);
		return cacheDto[0].getGifs();
	}

	private List<HistoryDto> historyParser(File file) {
		var historyList = new ArrayList<HistoryDto>();
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)))) {
			String line;
			while ((line = reader.readLine()) != null) {
				var history = new HistoryDto();
				String[] result = line.split(",");
				history.setDate(LocalDate.parse(result[0]));
				history.setQuery(result[1]);
				history.setGif(result[2]);
				historyList.add(history);
			}
		}
		catch (IOException ex) {
			System.err.println("Impossible to read file");
		}

		return historyList;
	}

	public HistoryDto[] parseHistory(File file) {
		var resultList = historyParser(file);
		var resultArray = new HistoryDto[resultList.size()];
		return resultList.toArray(resultArray);
	}

}