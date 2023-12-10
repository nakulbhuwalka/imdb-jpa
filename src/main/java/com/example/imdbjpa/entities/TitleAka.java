package com.example.imdbjpa.entities;

import com.example.imdbjpa.DataReader;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Entity
@IdClass(TitleAkaId.class)
public class TitleAka {
	@Id
	private String titleId;
	@Id
	private int ordering;
	@Column(length = 1024)
	private String title;
	private String region;
	private String language;
	private String types;
	private String attributes;
	private boolean isOriginalTitle;

	public static TitleAka map(String[] in) {
		
		return TitleAka.builder()
				.titleId(in[0])
				.ordering(DataReader.getInt(in[1]))
				.title(DataReader.getStr(in[2]))
				.region(DataReader.getStr(in[3]))
				.language(DataReader.getStr(in[4]))
				.types(DataReader.getStr(in[5]))
				.attributes(DataReader.getStr(in[6]))
				.isOriginalTitle(DataReader.getBool(in[7]))
				
				.build();

	}
}
