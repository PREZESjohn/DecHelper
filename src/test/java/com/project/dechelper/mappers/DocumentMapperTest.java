package com.project.dechelper.mappers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ai.document.Document;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class DocumentMapperTest {

    @Mock
    private ResultSet resultSet;

    @Test
    public void mapRowTest() throws SQLException {
        Mockito.when(resultSet.getString("id")).thenReturn("myId");
        Mockito.when(resultSet.getString("content")).thenReturn("myContent");
        Mockito.when(resultSet.getString("metadata")).thenReturn("{\"meta\": \"data\"}");

        DocumentMapper documentMapper = new DocumentMapper();
        Document document = documentMapper.mapRow(resultSet, 0);

        assertNotNull(document);
        assertEquals("myId", document.getId());
        assertEquals("myContent", document.getText());
        assertEquals(new HashMap<>(Map.of("meta","data")), document.getMetadata());
    }
}
