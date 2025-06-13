package com.project.dechelper.toolCalling;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.ai.document.Document;
import org.springframework.ai.rag.Query;
import org.springframework.ai.rag.retrieval.search.DocumentRetriever;
import org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.filter.FilterExpressionBuilder;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class DataRetrivalTools {

    @Tool(description = "Current date and time in the user's timezone")
    public String getCurrentDateTime(){
        System.out.println("------------uruchomienie toola---------------");
        return LocalDateTime.now().atZone(LocaleContextHolder.getTimeZone().toZoneId()).toString();
    }
    //    private final VectorStore vectorStore;
//
//    @Tool(description = "Get data from RAG about informations from user prompt")
//    public String getRelevantData(String query){
//        DocumentRetriever retriever = VectorStoreDocumentRetriever.builder()
//                .vectorStore(vectorStore)
//                .similarityThreshold(0.55)
//                .topK(20)
//                .build();
//        List<Document> documents = retriever.retrieve(new Query(query));
//        String documentsString = documents.stream().map(Document::getFormattedContent).collect(Collectors.joining("\n"));
//        System.out.println("Documents from RAG: "+documents);
//        return documentsString;
//    }
}
