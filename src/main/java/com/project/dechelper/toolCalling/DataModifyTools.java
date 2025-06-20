package com.project.dechelper.toolCalling;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.document.Document;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.vectorstore.VectorStore;

@RequiredArgsConstructor
public class DataModifyTools {

    private final VectorStore vectorStore;

    @Tool(description = "Put document in RAG vector store")
    public String putDataInStore(Document document) {
        return "Document added successfully";
    }

    @Tool(description = "Update document in RAG vector store")
    public String updateDataInStore(Document document) {
        return "Document updated successfully";
    }

    @Tool(description = "Delete document in RAG vector store")
    public String deleteDataInStore(String documentId) {
        return "Document deleted successfully";
    }
}
