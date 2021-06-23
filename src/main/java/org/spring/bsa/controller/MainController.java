package org.spring.bsa.controller;

import org.spring.bsa.dto.CacheDto;
import org.spring.bsa.dto.Query;
import org.spring.bsa.entities.Cache;
import org.spring.bsa.service.FileSystemService;
import org.spring.bsa.service.GiphyService;
import org.spring.bsa.service.ParserService;
import org.spring.bsa.utils.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {

	GiphyService giphyService;

	FileSystemService fileSystemService;

	Cache cache;

	UserUtil userUtil;

	ParserService parserService;

	CacheDto cacheDto;

	@Autowired
	public MainController(GiphyService giphyService, FileSystemService fileSystemService, Cache cache,
			UserUtil userUtil, ParserService parserService, CacheDto cacheDto) {
		this.giphyService = giphyService;
		this.fileSystemService = fileSystemService;
		this.cache = cache;
		this.userUtil = userUtil;
		this.parserService = parserService;
		this.cacheDto = cacheDto;
	}

	@PostMapping("/cache/generate")
	public ResponseEntity<?> getGif(@RequestBody Query query) {
		var giphyApi = giphyService.searchGif(null, query);
		fileSystemService.downloadGif(giphyApi);

		CacheDto cacheDto = parserService.parseCache(fileSystemService.getAllGifFromCache(query.getQuery()))[0];
		return new ResponseEntity<>(cache, HttpStatus.OK);
	}

	@GetMapping("/gifs")
	public ResponseEntity<?> getGifs() {
		var gifsArray = parserService.parseOnlyFiles(fileSystemService.getAllGifFromCache(null));
		return new ResponseEntity<>(gifsArray, HttpStatus.OK);
	}

	@GetMapping("/cache")
	public ResponseEntity<?> getCache(String query) {
		var fullCache = parserService.parseCache(fileSystemService.getAllGifFromCache(query));
		return new ResponseEntity<>(fullCache, HttpStatus.OK);
	}

}
