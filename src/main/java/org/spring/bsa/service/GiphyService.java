package org.spring.bsa.service;

import org.json.JSONObject;
import org.spring.bsa.dto.GifFileDto;
import org.spring.bsa.dto.Query;
import org.spring.bsa.entities.GifEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class GiphyService {

	Environment environment;

	@Autowired
	public GiphyService(Environment environment) {
		this.environment = environment;
	}

	public GifEntity searchGif(String user_id, Query query) {
		RestTemplate restTemplate = new RestTemplate();
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(environment.getProperty("giphy.search.url"))
				.queryParam("api_key", environment.getProperty("giphy.api.key")).queryParam("tag", query.getQuery())
				.queryParam("random_id", user_id);

		GifFileDto gifFile = restTemplate.getForObject(builder.toUriString(), GifFileDto.class);

		JSONObject jsonObject = new JSONObject(gifFile);
		jsonObject = jsonObject.getJSONObject("data");

		GifEntity gifEntity = new GifEntity();
		gifEntity.setId(jsonObject.getString("id"));

		StringBuilder url = new StringBuilder(
				jsonObject.getJSONObject("images").getJSONObject("downsized").getString("url"));
		url.replace(8, 14, "i");

		gifEntity.setUrl(url.toString());
		gifEntity.setQuery(query.getQuery());

		return gifEntity;
	}

}
