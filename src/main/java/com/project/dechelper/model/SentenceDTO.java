package com.project.dechelper.model;

import lombok.*;

public record SentenceDTO (String sentence, Double similarityThreshold, int returnInfoAmount){
    public SentenceDTO(String sentence){
        this(sentence, 0.5,2);
    }
}
