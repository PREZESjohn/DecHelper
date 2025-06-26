package com.project.dechelper.services;

import com.project.dechelper.mappers.DocumentMapper;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.filter.Filter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SimplePropertyRowMapper;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DocumentServiceTest {
    @Mock
    private VectorStore vectorStore;
    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private DocumentServiceImpl serviceUnderTest;

    private final List<Document> DOCUMENTS = List.of(
            new Document("test1", Map.of("Subject","Subject1", "Type", "Type1")),
            new Document("test2", Map.of("Subject","Subject1", "Type", "Type2")),
            new Document("test3", Map.of("Subject","Subject2", "Type", "Type2"))
    );
    private final Document DOCUMENT_1 = new Document("test_one", Map.of("Subject","Subject1", "Type", "Type1"));

    @BeforeEach
    public void initiateVectorStore() {
        vectorStore.add(DOCUMENTS);
    }
    @AfterEach
    public void cleanupVectorStore() {
        vectorStore = null;
    }

    @Test
    public void getAllDocsTest(){
        doReturn(DOCUMENTS).when(jdbcTemplate).query(any(String.class), any(DocumentMapper.class));
        List<Document> docs = serviceUnderTest.getAllDocs();
        assertEquals(DOCUMENTS, docs);
        verify(jdbcTemplate).query(any(String.class), any(DocumentMapper.class));
    }

    @Test
    public void getAllDocsBySubjectTest(){
        doReturn(DOCUMENTS.subList(0,2)).when(jdbcTemplate).query(any(String.class), any(DocumentMapper.class));
        List<Document> docs = serviceUnderTest.getAllDocsBySubject("Subject1");
        assertEquals(DOCUMENTS.subList(0,2), docs);
        verify(jdbcTemplate).query(any(String.class), any(DocumentMapper.class));
    }

    @Test
    public void saveDocTest(){
        serviceUnderTest.saveDoc(DOCUMENT_1);
        verify(vectorStore).add(List.of(DOCUMENT_1));
    }

    @Test
    public void updateDocTest(){
        serviceUnderTest.updateDoc(DOCUMENT_1);
        verify(vectorStore).delete(List.of(DOCUMENT_1.getId()));
        verify(vectorStore).add(List.of(DOCUMENT_1));
    }

    @Test
    public void deleteDocByIdTest(){
        serviceUnderTest.deleteDocById(DOCUMENTS.get(0).getId());
        verify(vectorStore).delete(List.of(DOCUMENTS.get(0).getId()));
    }

    @Test
    public void deleteAllDocsBySubjectTest(){
        serviceUnderTest.deleteAllDocsBySubject("Subject1");
        verify(vectorStore).delete(any(Filter.Expression.class));
    }
}
