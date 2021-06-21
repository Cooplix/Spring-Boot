package org.spring.bsa.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class CacheDto {

	private String query;

	private String[] gifs;

}
