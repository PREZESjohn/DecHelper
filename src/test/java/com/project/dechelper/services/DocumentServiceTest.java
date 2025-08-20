package com.project.dechelper.services;

import com.project.dechelper.mappers.DocumentDTOMapper;
import com.project.dechelper.mappers.DocumentMapper;
import com.project.dechelper.model.DocumentDTO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.filter.Filter;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.HashMap;
import java.util.LinkedHashMap;
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

    private final List<Document> documentList = List.of(
            new Document("28ff3e34-c352-4389-a9af-5b578137a73b","test1", Map.of("Subject","Subject1", "Type", "Type1")),
            new Document("38588d29-e7fe-4942-804c-0241128585a9","test2", Map.of("Subject","Subject1", "Type", "Type2")),
            new Document("0dec77be-8c8c-4ade-bf09-08d8d1a187b7","test3", Map.of("Subject","Subject2", "Type", "Type2"))
    );
    private final List<DocumentDTO> documentDTOList = List.of(
            new DocumentDTO("28ff3e34-c352-4389-a9af-5b578137a73b","test1", Map.of("Subject","Subject1", "Type", "Type1")),
            new DocumentDTO("38588d29-e7fe-4942-804c-0241128585a9","test2", Map.of("Subject","Subject1", "Type", "Type2")),
            new DocumentDTO("0dec77be-8c8c-4ade-bf09-08d8d1a187b7", "test3", Map.of("Subject","Subject2", "Type", "Type2"))
    );
    private final Document DOCUMENT_1 = new Document("1","test_one", new LinkedHashMap<>(Map.of("Subject","Subject1", "Type", "Type1")));
    private final DocumentDTO DOCUMENTDTO_1 = new DocumentDTO("1","test_one",  new LinkedHashMap<>(Map.of("Subject","Subject1", "Type", "Type1")));

    @Test
    public void getAllDocsTest(){
        doReturn(documentList).when(jdbcTemplate).query(any(String.class), any(DocumentDTOMapper.class));
        List<DocumentDTO> docs = serviceUnderTest.getAllDocs();
        assertEquals(documentList, docs);
        verify(jdbcTemplate).query(any(String.class), any(DocumentDTOMapper.class));
    }

    @Test
    public void getAllDocsBySubjectTest(){
        doReturn(documentList.subList(0,2)).when(jdbcTemplate).query(any(String.class), any(DocumentDTOMapper.class));
        List<DocumentDTO> docs = serviceUnderTest.getAllDocsBySubject("Subject1");
        assertEquals(documentList.subList(0,2), docs);
        verify(jdbcTemplate).query(any(String.class), any(DocumentDTOMapper.class));
    }

    @Test
    public void saveDocTest(){
        serviceUnderTest.saveDoc(DOCUMENTDTO_1);
        verify(vectorStore).add(anyList());
    }

    @Test
    public void updateDocTest(){
        serviceUnderTest.updateDoc(DOCUMENTDTO_1);
        verify(vectorStore).delete(List.of(DOCUMENTDTO_1.id()));
        verify(vectorStore).add(List.of(DOCUMENT_1));
    }

    @Test
    public void deleteDocByIdTest(){
        serviceUnderTest.deleteDocById(documentList.get(0).getId());
        verify(vectorStore).delete(List.of(documentList.get(0).getId()));
    }

    @Test
    public void deleteAllDocsBySubjectTest(){
        serviceUnderTest.deleteAllDocsBySubject("Subject1");
        verify(vectorStore).delete(any(Filter.Expression.class));
    }
}
