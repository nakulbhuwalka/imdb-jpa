package com.example.imdbjpa.entities;

import org.springframework.data.domain.Persistable;
import org.springframework.lang.Nullable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Data;

@Entity
@Data
@Builder
public class TitleBasics extends AbstractEntity<String> {

    @Id
    private String tconst;
    private String titleType;
    @Column(length=1024)
    private String primaryTitle;
    @Column(length=1024)
    private String originalTitle;
    private boolean isAdult;
    private int startYear;
    private int endYear;
    private long runtimeMinutes;
    private String genres;

    @Override
    @Nullable
    public String getId() {
       return getTconst();
    }

}
