package com.project.dechelper.mappers;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.ai.document.Document;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class DocumentMapper implements RowMapper<Document> {

    @SneakyThrows
    @Override
    public Document mapRow(ResultSet rs, int rowNum) throws SQLException {
        Map<String, Object> mapping = new ObjectMapper().readValue(rs.getString("metadata"), HashMap.class);
        Document dc = new Document(
                rs.getString("id"),
                rs.getString("content"),
                mapping);
        return dc;
    }
}
