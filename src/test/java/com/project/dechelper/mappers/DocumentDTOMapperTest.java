package com.project.dechelper.mappers;

import com.project.dechelper.model.DocumentDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ai.document.Document;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DocumentDTOMapperTest {

    @Mock
    private ResultSet resultSet;

    @Test
    public void mapRowTest() throws SQLException {
        when(resultSet.getString("id")).thenReturn("myId");
        when(resultSet.getString("content")).thenReturn("myContent");
        when(resultSet.getString("metadata")).thenReturn("{\"meta\": \"data\"}");

        DocumentDTOMapper documentMapper = new DocumentDTOMapper();
        DocumentDTO document = documentMapper.mapRow(resultSet, 0);

        assertNotNull(document);
        assertEquals("myId", document.id());
        assertEquals("myContent", document.text());
        assertEquals(new HashMap<>(Map.of("meta","data")), document.metadata());
    }
}
