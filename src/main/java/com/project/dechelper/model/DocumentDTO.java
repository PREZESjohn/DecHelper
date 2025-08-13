package com.project.dechelper.model;

import java.util.List;
import java.util.Map;

public record DocumentDTO(String id, String text, Map<String,Object> metadata){
}


