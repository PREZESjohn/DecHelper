package com.project.dechelper.model;


public record SentenceDTO (String sentence, Double similarityThreshold, int returnInfoAmount){
    public SentenceDTO {
        if (similarityThreshold == null) {
            similarityThreshold = 0.55;
        }
        if (returnInfoAmount == 0) {
            returnInfoAmount = 3;
        }
    }
}
