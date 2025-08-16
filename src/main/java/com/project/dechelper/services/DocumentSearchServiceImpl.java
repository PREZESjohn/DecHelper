package com.project.dechelper.services;

import com.project.dechelper.model.SentenceDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
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
                        .query(sentence.sentence())
                        .similarityThreshold(sentence.similarityThreshold())
                        .topK(sentence.returnInfoAmount())
                        .build()
        );
    }
}
