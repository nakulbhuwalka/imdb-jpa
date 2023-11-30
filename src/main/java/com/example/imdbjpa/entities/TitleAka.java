package com.example.imdbjpa.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class TitleAka {
    @Id
    private String titleId;
    private int ordering;
    @Column(length = 1024)
    private String title;
    private String region;
    private String language;
    private String types;
    private String attributes;
    private boolean isOriginalTitle;
}
