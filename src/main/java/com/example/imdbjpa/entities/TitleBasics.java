package com.example.imdbjpa.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class TitleBasics {

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
}
