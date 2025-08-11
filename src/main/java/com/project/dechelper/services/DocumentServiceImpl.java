package com.project.dechelper.services;

import com.project.dechelper.mappers.DocumentDTOMapper;
import com.project.dechelper.mappers.DocumentMapper;
import com.project.dechelper.model.DocumentDTO;
import jakarta.websocket.OnClose;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.filter.Filter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DocumentServiceImpl implements DocumentService {
    private final VectorStore vectorStore;
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<DocumentDTO> getAllDocs() {
        return jdbcTemplate.query("select *\n" +
                    "from vector_store;",new DocumentDTOMapper());
    }

    @Override
    public List<DocumentDTO> getAllDocsBySubject(String subject) {
        return jdbcTemplate.query("select *\n" +
                "from vector_store where (metadata->>'Subject') = '"+subject+"';",new DocumentDTOMapper());
    }

    @Override
    public Page<DocumentDTO> getAllDocsByPage(Pageable pageable) {
        int total = jdbcTemplate.queryForObject("select count(1)" +
                "from vector_store;", (rs, rowNum) -> rs.getInt(1));

        List<DocumentDTO> documentsByPage = jdbcTemplate.query("select *\n" +
                "from vector_store "+
                "LIMIT "+pageable.getPageSize()+" "+
                "OFFSET "+pageable.getOffset()+";",new DocumentDTOMapper());
        return new PageImpl<>(documentsByPage,pageable,total);
    }

    @Override
    public void saveDoc(DocumentDTO document) {
        Map<String, Object> metadata = document.metadata().stream().collect(Collectors.toMap(
                DocumentDTO.Metadata::key,DocumentDTO.Metadata::value
        ));
        StringBuilder combinedText = new StringBuilder();
        for(Object value : metadata.values()) {
            if(value instanceof String) {
                combinedText.append(value).append(" | ");
            }
        }
        combinedText.append(document.text());
        vectorStore.add(List.of(new Document(combinedText.toString(),metadata)));
    }

    @Override
    public void updateDoc(DocumentDTO document) {
        Map<String, Object> metadata = document.metadata().stream().collect(Collectors.toMap(
                DocumentDTO.Metadata::key,DocumentDTO.Metadata::value
        ));
        vectorStore.delete(List.of(document.id()));
        vectorStore.add(List.of(new Document(document.id(),document.text(),metadata)));
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
