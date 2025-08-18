package utils;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.project.dechelper.model.DocumentDTO;
import org.springframework.ai.document.Document;

import java.io.IOException;
import java.util.Map;

public class DocumentDTODeserializer extends StdDeserializer<DocumentDTO> {
    public DocumentDTODeserializer() {
        this(null);
    }

    public DocumentDTODeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public DocumentDTO deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JacksonException {
        JsonNode node = jp.getCodec().readTree(jp);
        String id = node.has("id") ? node.get("id").asText() : null;
        String text = node.has("text") ? node.get("text").asText() : null;
        JsonNode metadataNode = node.has("metadata") ? node.get("metadata") : null;

        // Walidacja
        if (id == null) {
            throw new IOException("id must be specified");
        }
        if (text == null) {
            throw new IOException("text must be specified");
        }
        if (metadataNode == null) {
            throw new IOException("metadata must be specified");
        }

        // Deserializacja metadata jako Map
        Map<String, Object> metadata = jp.getCodec().treeToValue(metadataNode, Map.class);

        // Użycie konstruktora Document(String text, Map<String, Object> metadata)
        return new DocumentDTO(id, text, metadata);
    }
}