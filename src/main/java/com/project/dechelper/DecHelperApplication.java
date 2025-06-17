package com.project.dechelper;

import com.project.dechelper.toolCalling.DataRetrivalTools;
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
    public DataRetrivalTools dataRetrivalTools(VectorStore vectorStore) {
        return new DataRetrivalTools(vectorStore);
    }

}
