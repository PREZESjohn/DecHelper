package com.project.dechelper.services;

import com.project.dechelper.model.Information;
import com.project.dechelper.model.SentenceDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DocumentSearchServiceImpl implements DocumentSearchService {

    private final VectorStore vectorStore;

    @Override
    public List<Document> getRelevantData(SentenceDTO sentence) {
        return vectorStore.similaritySearch(
                SearchRequest.builder()
                        .query(sentence.getSentence())
                        .similarityThreshold(sentence.getSimilarityThreshold())
                        .topK(sentence.getReturnInfoAmount())
                        .build()
        );
    }
}
