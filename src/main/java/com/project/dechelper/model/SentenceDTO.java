package com.project.dechelper.model;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class SentenceDTO {
    private String sentence;
    private int returnInfoAmount;
}
