package com.project.dechelper.toolCalling;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import utils.FakeVectorStore;

import java.time.ZoneId;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class DataRetrievalToolsTest {

    private DataRetrievalTools dataRetrievalTools;

    private final List<Document> documentList = List.of(
            new Document("28ff3e34-c352-4389-a9af-5b578137a73b","test1", Map.of("Subject","Subject1", "Type", "Type1")),
            new Document("38588d29-e7fe-4942-804c-0241128585a9","test2", Map.of("Subject","Subject1", "Type", "Type2")),
            new Document("0dec77be-8c8c-4ade-bf09-08d8d1a187b7", "test3", Map.of("Subject","Subject2", "Type", "Type2"))
    );

    @BeforeEach
    public void setUp() {
        VectorStore vectorStore = new FakeVectorStore();
        dataRetrievalTools=new DataRetrievalTools(vectorStore);
    }
    @Test
    public void getRelevantData_Test(){
        String result = dataRetrievalTools.getRelevantData("test query");

        assertTrue(result.contains("test1"));
        assertTrue(result.contains("test2"));
        assertTrue(result.contains("test3"));
    }

    @Test
    public void getDataByTimeRange_Test(){
        String result = dataRetrievalTools.getDataByTimeRange("test query","startDate","endDate");

        assertTrue(result.contains("test1"));
        assertTrue(result.contains("test2"));
        assertTrue(result.contains("test3"));
    }

    @Test
    void getCurrentDateTime_Test() {
        String result = dataRetrievalTools.getCurrentDateTime();
        assertNotNull(result);
        assertTrue(result.contains("T"));
        assertTrue(result.contains(ZoneId.systemDefault().getId().substring(0, 2)));
    }
}
