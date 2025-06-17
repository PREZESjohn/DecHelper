package com.project.dechelper.services;

import com.project.dechelper.model.Information;
import org.springframework.ai.document.Document;

import java.util.List;


public interface DocumentService {

    List<Document> getAllDocs();
    List<Document> getAllDocsBySubject(String subject);

    void saveDoc(Document document);
    void deleteDocById(String id);
    void deleteAllDocsBySubject(String subject);

}
