package com.project.dechelper.services;

import com.project.dechelper.model.Information;
import org.springframework.ai.document.Document;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface DocumentService {

    List<Document> getAllDocs();
    List<Document> getAllDocsBySubject(String subject);
    Page<Document> getAllDocsByPage(Pageable pageable);

    void saveDoc(Document document);
    void updateDoc(Document document);
    void deleteDocById(String id);
    void deleteAllDocsBySubject(String subject);

}
