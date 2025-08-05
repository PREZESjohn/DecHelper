package com.project.dechelper.services;

import com.project.dechelper.model.DocumentDTO;
import com.project.dechelper.model.Information;
import org.springframework.ai.document.Document;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface DocumentService {

    List<DocumentDTO> getAllDocs();
    List<DocumentDTO> getAllDocsBySubject(String subject);
    Page<DocumentDTO> getAllDocsByPage(Pageable pageable);

    void saveDoc(DocumentDTO document);
    void updateDoc(DocumentDTO document);
    void deleteDocById(String id);
    void deleteAllDocsBySubject(String subject);

}
