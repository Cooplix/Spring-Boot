package org.spring.bsa.controller;

import org.spring.bsa.dto.HistoryDto;
import org.spring.bsa.entities.Cache;
import org.spring.bsa.service.FileSystemService;
import org.spring.bsa.service.GiphyService;
import org.spring.bsa.service.ParserService;
import org.spring.bsa.utils.UserUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

}
