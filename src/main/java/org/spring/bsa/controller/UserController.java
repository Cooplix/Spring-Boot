package org.spring.bsa.controller;

import org.spring.bsa.dto.HistoryDto;
import org.spring.bsa.dto.QueryDto;
import org.spring.bsa.entities.Cache;
import org.spring.bsa.service.FileSystemService;
import org.spring.bsa.service.GiphyService;
import org.spring.bsa.service.ParserService;
import org.spring.bsa.utils.UserUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user/")
public class UserController {

	GiphyService giphyService;

	FileSystemService fileSystemService;

	Cache cache;

	UserUtil userUtil;

	ParserService parserService;

	public UserController(GiphyService giphyService, FileSystemService fileSystemService, Cache cache,
			UserUtil userUtil, ParserService parserService) {
		this.giphyService = giphyService;
		this.fileSystemService = fileSystemService;
		this.cache = cache;
		this.userUtil = userUtil;
		this.parserService = parserService;
	}

	private ResponseEntity<?> validateUserID(String id) {
		if (userUtil.checkUserID(id)) {
			Map<String, String> tempResponse = new HashMap<>();
			tempResponse.put("message", "Invalid request");
			return new ResponseEntity<>(tempResponse, HttpStatus.BAD_REQUEST);
		}
		return null;
	}

	@GetMapping("{id/history}")
	public ResponseEntity<?> getUserHistory(@PathVariable String id) {
		if (validateUserID(id) != null) {
			return validateUserID(id);
		}

		HistoryDto[] result = parserService.parseHistory(fileSystemService.getHistory(id));

		if (result != null) {
			return new ResponseEntity<>(result, HttpStatus.OK);
		}
		else {
			return ResponseEntity.notFound().build();
		}
	}

	@DeleteMapping("{id}/history/clean")
	public ResponseEntity<?> deleteUserHistory(@PathVariable String id) {
		if (validateUserID(id) != null) {
			return validateUserID(id);
		}

		if (fileSystemService.deleteHistory(id)) {
			return ResponseEntity.ok().build();
		}
		else {
			return ResponseEntity.badRequest().build();
		}
	}

	@GetMapping("{id/all}")
	public ResponseEntity<?> getAllUserGif(@PathVariable String id) {
		if (validateUserID(id) != null) {
			return validateUserID(id);
		}

		var result = parserService.parseCache(fileSystemService.getAllUserGifFromCache(id));
		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	@PostMapping("{id/generate}")
	public ResponseEntity<?> getAllUserGif(@PathVariable String id, @RequestBody QueryDto query) {
		if (validateUserID(id) != null) {
			return validateUserID(id);
		}

		File gif;
		if (!query.getForce()) {
			gif = fileSystemService.getGifPath(query.getQuery());
			if (gif != null) {
				return new ResponseEntity<>(gif.getAbsolutePath(), HttpStatus.OK);
			}
		}
		var gifEntity = giphyService.searchGif(null, query);
		var resultFile = fileSystemService.copyGifToUserFolder(id, query.getQuery(),
				fileSystemService.downloadGif(gifEntity).getPath());

		if (resultFile == null) {
			return ResponseEntity.notFound().build();
		}

		gif = fileSystemService.getGifPath(query.getQuery());

		return new ResponseEntity<>(gif, HttpStatus.OK);
	}

	@DeleteMapping("{id/reset}")
	public ResponseEntity<?> resetCache(@PathVariable String id, String query) {
		if (validateUserID(id) != null) {
			return validateUserID(id);
		}

		if (query == null) {
			cache.resetUserCache(id);
		}
		else {
			cache.resetUserCache(id, query);
		}
		return ResponseEntity.ok().build();
	}

	@DeleteMapping("{id/clean}")
	public ResponseEntity<?> deleteUser(@PathVariable String id, String query) {
		if (validateUserID(id) != null) {
			return validateUserID(id);
		}

		cache.resetUserCache(id);
		fileSystemService.deleteUser(id);
		return ResponseEntity.ok().build();
	}

	@GetMapping("{id/search}")
	public ResponseEntity<?> searchGif(@PathVariable String id, String query, boolean force) {
		if (validateUserID(id) != null) {
			return validateUserID(id);
		}

		if (!force) {
			var gif = cache.getGif(id, query);
			if (gif != null) {
				return new ResponseEntity<>(gif, HttpStatus.OK);
			}
		}

		var gif = fileSystemService.getGifPath(query);

		if (gif == null) {
			return ResponseEntity.notFound().build();
		}

		cache.updateCache(id, query, gif.getAbsolutePath());
		return new ResponseEntity<>(gif.getAbsolutePath(), HttpStatus.OK);
	}

}
