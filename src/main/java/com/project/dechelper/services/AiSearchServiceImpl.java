package com.project.dechelper.services;

import com.project.dechelper.model.Information;
import com.project.dechelper.model.SentenceDTO;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AiSearchServiceImpl implements AiSearchService {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Information> getRelevantData(SentenceDTO sentence) {
        String sql="WITH query_embedding AS (\n" +
                "    SELECT ai.ollama_embed('nomic-embed-text', '"+sentence.getSentence()+"', host => 'http://host.docker.internal:7869') AS embedding\n" +
                ")\n" +
                "SELECT\n" +
                "    m.id,\n" +
                "    m.subject,\n" +
                "    m.type,\n" +
                "   m.content,\n" +
                "    t.embedding <=> (SELECT embedding FROM query_embedding) AS distance\n" +
                "FROM informations_embeddings t\n" +
                "LEFT JOIN information m ON t.id = m.id\n" +
                "ORDER BY distance\n" +
                "LIMIT "+sentence.getReturnInfoAmount()+";";
        return jdbcTemplate.query(sql, (rs, rowNum) ->
                new Information(
                        rs.getInt("id"),
                        rs.getString("subject"),
                        rs.getString("type"),
                        rs.getString("content")
                )
        );
    }
}
