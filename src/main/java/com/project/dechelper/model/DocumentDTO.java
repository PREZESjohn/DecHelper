package com.project.dechelper.model;


import lombok.*;

import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class DocumentDTO {
    private String id;
    private String text;
    private Map<String,Object> metadata;
}
