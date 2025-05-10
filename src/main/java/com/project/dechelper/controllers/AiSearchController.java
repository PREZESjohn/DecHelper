package com.project.dechelper.controllers;

import com.project.dechelper.model.Information;
import com.project.dechelper.model.SentenceDTO;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/search")
@RequiredArgsConstructor
@Tag(name="Ai search controller")
public class AiSearchController {

    private final JdbcTemplate jdbcTemplate;

    @PostMapping("/sentence")
    public List<Information> findInfoBySentence(@RequestBody SentenceDTO sentence) {
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
