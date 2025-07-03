package com.project.dechelper.controllers;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.project.dechelper.services.DocumentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import utils.DocumentDeserializer;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = DocumentController.class)
@ExtendWith(MockitoExtension.class)
public class DocumentControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private DocumentService documentService;

    @MockitoBean
    private VectorStore vectorStore;

    @InjectMocks
    private DocumentController underTest;

    private final List<Document> DOCUMENTS = List.of(
            new Document("test1", Map.of("Subject","Subject1", "Type", "Type1")),
            new Document("test2", Map.of("Subject","Subject1", "Type", "Type2")),
            new Document("test3", Map.of("Subject","Subject2", "Type", "Type2"))
    );
    private final Document DOCUMENT_1 = new Document("test_one", Map.of("Subject","Subject1", "Type", "Type1"));


    @Test
    public void getDocsTest() throws Exception {
        Mockito.when(documentService.getAllDocs()).thenReturn(DOCUMENTS);
        MvcResult resultMvc = mockMvc.perform(get("/api/v1/docs/all"))
                .andExpect(status().isOk())
                .andReturn();

        ObjectMapper objectMapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(Document.class, new DocumentDeserializer());
        objectMapper.registerModule(module);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        List<Document> result = objectMapper.readValue(resultMvc.getResponse().getContentAsString(),
                new TypeReference<List<Document>>() {});

        assertEquals(DOCUMENTS.size(), result.size());
        assertEquals(DOCUMENTS.get(0).getText(), result.get(0).getText());
        assertEquals(DOCUMENTS.get(1).getText(), result.get(1).getText());
        assertEquals(DOCUMENTS.get(2).getText(), result.get(2).getText());
        verify(documentService).getAllDocs();
    }

    @Test
    public void getDocsBySubjectTest() throws Exception {
        Mockito.when(documentService.getAllDocsBySubject(any(String.class))).thenReturn(DOCUMENTS.subList(0,2));
        MvcResult resultMvc = mockMvc.perform(get("/api/v1/docs/get-subject/"+"Subject1"))
                .andExpect(status().isOk())
                .andReturn();

        ObjectMapper objectMapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(Document.class, new DocumentDeserializer());
        objectMapper.registerModule(module);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        List<Document> result = objectMapper.readValue(resultMvc.getResponse().getContentAsString(),
                new TypeReference<List<Document>>() {});

        assertEquals(DOCUMENTS.subList(0,2).size(), result.size());
        assertEquals(DOCUMENTS.get(0).getText(), result.get(0).getText());
        assertEquals(DOCUMENTS.get(1).getText(), result.get(1).getText());
        verify(documentService).getAllDocsBySubject(any(String.class));
    }
    @Test
    public void saveDocTest() throws Exception {
        mockMvc.perform(get("/api/v1/docs/add"))
                .andExpect(status().isOk());
    }
    @Test
    public void updateDocTest() throws Exception {
        mockMvc.perform(get("/api/v1/docs/update"))
                .andExpect(status().isOk());
    }
    @Test
    public void deleteDocTest() throws Exception {
        mockMvc.perform(get("/api/v1/docs/delete-id/1"))
                .andExpect(status().isOk());
    }
    @Test
    public void deleteDocsBySubjectTest() throws Exception {
        mockMvc.perform(get("/api/v1/docs/delete-subject/Subject1"))
                .andExpect(status().isOk());
    }
}
