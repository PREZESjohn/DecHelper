package com.project.dechelper.model;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class SentenceDTO {
    private String sentence;
    @Builder.Default
    private Double similarityThreshold=0.5;
    @Builder.Default
    private int returnInfoAmount=2;
}
