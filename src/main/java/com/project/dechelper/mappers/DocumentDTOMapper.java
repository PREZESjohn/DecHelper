package com.project.dechelper.mappers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.project.dechelper.model.DocumentDTO;
import lombok.SneakyThrows;
import org.springframework.ai.document.Document;
import org.springframework.jdbc.core.RowMapper;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DocumentDTOMapper implements RowMapper<DocumentDTO> {

    @SneakyThrows
    @Override
    public DocumentDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        Map<String, Object> mapping = new ObjectMapper().readValue(rs.getString("metadata"), HashMap.class);
        DocumentDTO dc = new DocumentDTO(
                rs.getString("id"),
                rs.getString("content"),
                mapping);
        return dc;
    }
}
