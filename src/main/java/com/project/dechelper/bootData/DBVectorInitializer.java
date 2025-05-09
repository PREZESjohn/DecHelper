package com.project.dechelper.bootData;

import jakarta.persistence.Access;
import lombok.Builder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@Builder
@Order(2)
public class DBVectorInitializer implements ApplicationRunner {

    private JdbcTemplate jdbcTemplate;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        String enableExtSQl = "CREATE EXTENSION IF NOT EXISTS vector CASCADE; \n" +
                "CREATE EXTENSION IF NOT EXISTS ai CASCADE;";
        int enableExtRet = jdbcTemplate.update(enableExtSQl);
        if(enableExtRet > 0) {
            System.out.println("Extensions enabled");
        }
        String createVectorizeSQl = "SELECT ai.create_vectorizer(\n" +
                "   'information'::regclass,\n" +
                "   destination => 'informations_embeddings',\n" +
                "   embedding => ai.embedding_ollama('nomic-embed-text', 768),\n" +
                "   chunking => ai.chunking_recursive_character_text_splitter('content')\n" +
                ");";
        jdbcTemplate.execute(createVectorizeSQl);
    }
}
