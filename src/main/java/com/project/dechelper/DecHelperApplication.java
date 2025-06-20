package com.project.dechelper;

import com.project.dechelper.services.DocumentService;
import com.project.dechelper.toolCalling.DataModifyTools;
import com.project.dechelper.toolCalling.DataRetrievalTools;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DecHelperApplication {


    public static void main(String[] args) {
        SpringApplication.run(DecHelperApplication.class, args);
    }

    @Bean
    public DataRetrievalTools dataRetrivalTools(VectorStore vectorStore) {
        return new DataRetrievalTools(vectorStore);
    }

    @Bean
    public DataModifyTools dataModifyTools(DocumentService documentService) {
        return new DataModifyTools(documentService);
    }

}
