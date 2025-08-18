package com.project.dechelper.controllers;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.project.dechelper.model.DocumentDTO;
import com.project.dechelper.services.DocumentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.filter.Filter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import utils.DocumentDTODeserializer;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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

    private final List<DocumentDTO> documentDTOList = List.of(
            new DocumentDTO("28ff3e34-c352-4389-a9af-5b578137a73b","test1", Map.of("Subject","Subject1", "Type", "Type1")),
            new DocumentDTO("38588d29-e7fe-4942-804c-0241128585a9","test2", Map.of("Subject","Subject1", "Type", "Type2")),
            new DocumentDTO("0dec77be-8c8c-4ade-bf09-08d8d1a187b7", "test3", Map.of("Subject","Subject2", "Type", "Type2"))
    );
    private final List<Document> documentList = List.of(
            new Document("28ff3e34-c352-4389-a9af-5b578137a73b","test1", Map.of("Subject","Subject1", "Type", "Type1")),
            new Document("38588d29-e7fe-4942-804c-0241128585a9","test2", Map.of("Subject","Subject1", "Type", "Type2")),
            new Document("0dec77be-8c8c-4ade-bf09-08d8d1a187b7", "test3", Map.of("Subject","Subject2", "Type", "Type2"))
    );
    private final DocumentDTO DOCUMENT_1 = new DocumentDTO("1","test_one", Map.of("Subject","Subject1", "Type", "Type1"));
    private final String DOCUMENT_JSON = """
            {
            "id":"1",
            "content":"he borrowed $123 from me.",
            "metadata":{
               "type":"Family",
               "Subject":"Cousin Martin"
            }
            }
            """;
    private final String update_DOCUMENT_JSON = """
            {
            "id":"1",
            "content":"updated value",
            "metadata":{
               "type":"Family",
               "Subject":"Cousin Martin"
            }
            }
            """;

    @Test
    public void getDocsTest() throws Exception {
        Mockito.when(documentService.getAllDocs()).thenReturn(documentDTOList);
        String result = mockMvc.perform(get("/api/v1/docs/all"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();


        assertTrue(documentDTOList.toString().contains("test1"));
        assertTrue(documentDTOList.toString().contains("test2"));
        assertTrue(documentDTOList.toString().contains("test3"));
        verify(documentService).getAllDocs();
    }

    @Test
    public void getDocsBySubjectTest() throws Exception {
        Mockito.when(documentService.getAllDocsBySubject(any(String.class))).thenReturn(documentDTOList.subList(0,2));
        String result = mockMvc.perform(get("/api/v1/docs/get-subject/"+"Subject1"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertTrue(documentDTOList.toString().contains("Subject1"));
        assertTrue(documentDTOList.toString().contains("test1"));
        assertTrue(documentDTOList.toString().contains("test2"));
        verify(documentService).getAllDocsBySubject(any(String.class));
    }
    @Test
    public void saveDocTest() throws Exception {
        mockMvc.perform(
                put("/api/v1/docs/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(DOCUMENT_JSON))
                .andExpect(status().isOk());

        verify(documentService).saveDoc(any(DocumentDTO.class));
    }
    @Test
    public void updateDocTest() throws Exception {
        mockMvc.perform(
                put("/api/v1/docs/update")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(update_DOCUMENT_JSON))
                .andExpect(status().isOk());

        verify(documentService).updateDoc(any(DocumentDTO.class));
    }
    @Test
    public void deleteDocTest() throws Exception {
        mockMvc.perform(
                delete("/api/v1/docs/delete-id/1").content("1"))
                .andExpect(status().isOk());

        verify(documentService).deleteDocById(any(String.class));
    }
    @Test
    public void deleteDocsBySubjectTest() throws Exception {
        mockMvc.perform(
                delete("/api/v1/docs/delete-subject/Subject1").content("Subject1"))
                .andExpect(status().isOk());

        verify(documentService).deleteAllDocsBySubject(any(String.class));
    }
}
