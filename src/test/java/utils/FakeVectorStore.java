package utils;

import org.springframework.ai.document.Document;
import org.springframework.ai.rag.Query;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.filter.Filter;

import java.util.List;
import java.util.Map;

public class FakeVectorStore implements VectorStore {

    private final List<Document> documentList = List.of(
            new Document("28ff3e34-c352-4389-a9af-5b578137a73b","test1", Map.of("Subject","Subject1", "Type", "Type1")),
            new Document("38588d29-e7fe-4942-804c-0241128585a9","test2", Map.of("Subject","Subject1", "Type", "Type2")),
            new Document("0dec77be-8c8c-4ade-bf09-08d8d1a187b7", "test3", Map.of("Subject","Subject2", "Type", "Type2"))
    );

    @Override
    public void add(List<Document> documents) {}

    @Override
    public void delete(List<String> idList) {}

    @Override
    public void delete(Filter.Expression filterExpression) {}

    @Override
    public List<Document> similaritySearch(SearchRequest request) {
        return documentList;
    }

}
