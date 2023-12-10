package com.example.imdbjpa.entities;

import lombok.Data;

import java.io.Serializable;

@Data
public class TitleAkaId implements Serializable {

    private String titleId;
    private int ordering;
}
