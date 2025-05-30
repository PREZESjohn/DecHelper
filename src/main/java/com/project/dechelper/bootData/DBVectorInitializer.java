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
                "CREATE EXTENSION IF NOT EXISTS hstore CASCADE; \n" +
                "CREATE EXTENSION IF NOT EXISTS \"uuid-ossp\" CASCADE;";
        int enableExtRet = jdbcTemplate.update(enableExtSQl);
        if(enableExtRet > 0) {
            System.out.println("Extensions enabled");
        }
        String createTableSQL = "CREATE TABLE IF NOT EXISTS vector_store (\n" +
                "\tid uuid DEFAULT uuid_generate_v4() PRIMARY KEY,\n" +
                "\tcontent text,\n" +
                "\tmetadata json,\n" +
                "\tembedding vector(1536)\n" +
                ");";
        jdbcTemplate.execute(createTableSQL);
        String createIndex = "CREATE INDEX ON vector_store USING HNSW (embedding vector_cosine_ops);";
        jdbcTemplate.execute(createIndex);
    }
}
