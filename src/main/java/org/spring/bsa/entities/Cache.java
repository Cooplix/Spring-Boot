package org.spring.bsa.entities;

import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

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

		if (this.map.get(userId) != null) {
			if (this.map.get(userId).get(query) != null) {
				tempGifList = this.map.get(userId).get(query);
				tempGifList.add(tempGifList.size(), gifId);
				this.map.get((userId)).put(query, tempGifList);
			}
			else {
				tempGifList.add(gifId);
				this.map.get(userId).put(query, tempGifList);
			}
		}
		else {
			var userMap = new HashMap<String, ArrayList<String>>();
			tempGifList.add(gifId);
			userMap.put(query, tempGifList);
			this.map.put(userId, userMap);
		}
	}

	public String getGif(String userId, String query) {
		if (this.map.get(userId) != null && this.map.get(userId).get(query) != null) {
			var queriList = new ArrayList<String>();
			queriList = this.map.get(userId).get(query);
			return queriList.get(new Random().nextInt(queriList.size()));
		}
		return null;
	}

	public void resetUserCache(String userId) {
		this.map.remove(userId);
	}

	public void resetUserCache(String userId, String query) {
		this.map.get(userId).remove(query);
	}

}
