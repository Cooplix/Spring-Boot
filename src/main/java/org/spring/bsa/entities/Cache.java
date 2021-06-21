package org.spring.bsa.entities;

import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Cache {

	@Getter
	private Map<String, Map<String, ArrayList<String>>> map;

	private static Cache uniqCacheInstance;

	private Cache() {
		map = new HashMap<>();
	}

	public static Cache getInstance() {
		if (uniqCacheInstance == null) {
			uniqCacheInstance = new Cache();
		}
		return uniqCacheInstance;
	}

	public void updateCache(String userId, String query, String gifId) {
		var tempGifList = new ArrayList<String>();

		if (map.get(userId) != null) {
			if (map.get(userId).get(query) != null) {
				tempGifList = map.get(userId).get(query);
				tempGifList.add(tempGifList.size(), gifId);
				map.get((userId)).put(query, tempGifList);
			}
			else {
				tempGifList.add(gifId);
				map.get(userId).put(query, tempGifList);
			}
		}
		else {
			var userMap = new HashMap<String, ArrayList<String>>();
			tempGifList.add(gifId);
			userMap.put(query, tempGifList);
			map.put(userId, userMap);
		}
	}

}
