package com.project.dechelper.services;

import com.project.dechelper.model.Information;
import com.project.dechelper.model.SentenceDTO;
import org.springframework.ai.document.Document;

import java.util.List;

public interface DocumentSearchService {
    List<Document> getRelevantData(SentenceDTO sentence);
}
