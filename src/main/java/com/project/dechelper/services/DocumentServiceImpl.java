package com.project.dechelper.services;

import com.project.dechelper.mappers.DocumentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.filter.Filter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DocumentServiceImpl implements DocumentService {
    private final VectorStore vectorStore;
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Document> getAllDocs() {
        return jdbcTemplate.query("select *\n" +
                    "from vector_store;",new DocumentMapper());
    }

    @Override
    public List<Document> getAllDocsBySubject(String subject) {
        return jdbcTemplate.query("select *\n" +
                "from vector_store where (metadata->>'Subject') = '"+subject+"';",new DocumentMapper());
    }

    @Override
    public void saveDoc(Document document) {
        vectorStore.add(List.of(document));
    }

    @Override
    public void deleteDocById(String id) {
        vectorStore.delete(List.of(id));
    }

    @Override
    public void deleteAllDocsBySubject(String subject) {
        Filter.Expression filterExpression = new Filter.Expression(
                Filter.ExpressionType.EQ,
                new Filter.Key("Subject"),
                new Filter.Value(subject)
        );
        vectorStore.delete(filterExpression);
    }
}
