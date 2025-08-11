package com.project.dechelper.model;

import java.util.List;

public record DocumentDTO(String id, String text, List<Metadata> metadata){
    public record Metadata(String key, String value){}
}


