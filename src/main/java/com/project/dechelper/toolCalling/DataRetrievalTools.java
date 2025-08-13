package com.project.dechelper.toolCalling;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.document.Document;
import org.springframework.ai.rag.Query;
import org.springframework.ai.rag.retrieval.search.DocumentRetriever;
import org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.filter.FilterExpressionBuilder;
import org.springframework.context.i18n.LocaleContextHolder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class DataRetrievalTools {

    private final VectorStore vectorStore;
    Logger log = Logger.getLogger(DataRetrievalTools.class.getName());

    @Tool(description = "Get data from user diary")
    public String getRelevantData(@ToolParam(description = "Subject of user prompt") String query){
        DocumentRetriever retriever = VectorStoreDocumentRetriever.builder()
                .vectorStore(vectorStore)
                .similarityThreshold(0.55)
                .topK(20)
                .build();
        List<Document> documents = retriever.retrieve(new Query(query));
        String documentsString = documents.stream().map(Document::getText).collect(Collectors.joining("\n"));
        log.info("Query: "+query+" | Documents from RAG: "+documentsString);
        return documentsString;
    }

    @Tool(description = "Get data from specific time range")
    public String getDataByTimeRange(
            @ToolParam(description = "Subject of user prompt") String query,
            @ToolParam(description = "Start date of search") String startDate,
            @ToolParam(description = "End date of search") String endDate){

        FilterExpressionBuilder b = new FilterExpressionBuilder();

        DocumentRetriever retriever = VectorStoreDocumentRetriever.builder()
                .vectorStore(vectorStore)
                .similarityThreshold(0.55)
                .filterExpression(b.and(
                        b.gte("createdOn",startDate),
                        b.lte("createdOn",endDate)
                ).build())
                .topK(20)
                .build();
        List<Document> documents = retriever.retrieve(new Query(query));
        String documentsString = documents.stream().map(Document::getText).collect(Collectors.joining("\n"));
        log.info("Query: "+query+" | Time range: "+startDate+" - "+endDate+" | Documents from RAG: "+documentsString);
        return documentsString;
    }

    @Tool(description = "Current date and time in the user's timezone")
    public String getCurrentDateTime(){
        log.info("Date and time: "+LocalDateTime.now());
        return LocalDateTime.now().atZone(LocaleContextHolder.getTimeZone().toZoneId()).toString();
    }
}
