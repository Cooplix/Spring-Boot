package org.spring.bsa.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class HistoryDto {

	LocalDate date;

	String query;

	String gif;

}
