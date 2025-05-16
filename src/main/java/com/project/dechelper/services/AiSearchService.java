package com.project.dechelper.services;

import com.project.dechelper.model.Information;
import com.project.dechelper.model.SentenceDTO;

import java.util.List;

public interface AiSearchService {
    List<Information> getRelevantData(SentenceDTO sentence);
}
