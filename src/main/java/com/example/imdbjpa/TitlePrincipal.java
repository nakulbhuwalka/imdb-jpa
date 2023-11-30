package com.example.imdbjpa;

import org.springframework.lang.Nullable;

import com.example.imdbjpa.entities.AbstractEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Data;

@Entity
@Data
@Builder
public class TitlePrincipal extends AbstractEntity<String> {

    @Id
    private String tconst;
    private int ordering;
    private String nconst;
    private String category;
    @Column(length = 1024)
    private String job;
     @Column(length = 1024)
    private String characters;

    @Override
    @Nullable
    public String getId() {
       return getTconst();
    }

}
