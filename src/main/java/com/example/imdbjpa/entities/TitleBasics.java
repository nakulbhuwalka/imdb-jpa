package com.example.imdbjpa.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class TitleBasics {

    @Id
    private String tconst;
    private String titleType;
    private String primaryTitle;
    private String originalTitle;
    private boolean isAdult;
    private int startYear;
    private int endYear;
    private long runtimeMinutes;
    private String genres;
}
