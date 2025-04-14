package com.project.dechelper.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Information {
    @Id
    private int id;
    private String subject;
    private String type;
    private String content;

}
