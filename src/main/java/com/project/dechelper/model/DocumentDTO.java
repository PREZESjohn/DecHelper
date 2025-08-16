package com.project.dechelper.model;

import java.util.Map;

public record DocumentDTO(String id, String text, Map<String,Object> metadata){
}


