package utils;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.springframework.ai.document.Document;

import java.io.IOException;
import java.util.Map;

public class DocumentDeserializer extends StdDeserializer<Document> {
    public DocumentDeserializer() {
        this(null);
    }

    public DocumentDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Document deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JacksonException {
        JsonNode node = jp.getCodec().readTree(jp);
        String text = node.has("text") ? node.get("text").asText() : null;
        JsonNode metadataNode = node.has("metadata") ? node.get("metadata") : null;

        // Walidacja
        if (text == null) {
            throw new IOException("text must be specified");
        }
        if (metadataNode == null) {
            throw new IOException("metadata must be specified");
        }

        // Deserializacja metadata jako Map
        Map<String, Object> metadata = jp.getCodec().treeToValue(metadataNode, Map.class);

        // Użycie konstruktora Document(String text, Map<String, Object> metadata)
        return new Document(text, metadata);
    }
}