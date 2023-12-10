package com.example.imdbjpa.entities;

import java.util.ArrayList;
import java.util.List;

import org.springframework.lang.Nullable;

import com.example.imdbjpa.DataReader;

import jakarta.persistence.Entity;
import lombok.Builder;
import lombok.Data;

//@Entity
@Data
@Builder
public class TitleCrew extends AbstractEntity<String> {


	private String tconst ;
	private String nconst ;
	private String type ;
	@Override
	@Nullable
	public String getId() {
		// TODO Auto-generated method stub
		return getTconst();
	}
	
	
	public static List<TitleCrew> map(String[] in) {
		List<TitleCrew>  crew = new ArrayList<>();
		
		String tconst = in[0];
		
		for(String nconst : DataReader.getStrArr(in[1]))
		{
			crew.add(TitleCrew.builder().tconst(tconst).nconst(nconst).type("director").build());
		}
		for(String nconst : DataReader.getStrArr(in[2]))
		{
			crew.add(TitleCrew.builder().tconst(tconst).nconst(nconst).type("writer").build());
		}


		return crew;

	}

}
