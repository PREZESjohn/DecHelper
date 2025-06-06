package com.project.dechelper.controllers;

import com.project.dechelper.model.Information;
import com.project.dechelper.model.SentenceDTO;
import com.project.dechelper.services.AiSearchService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.hibernate.sql.ast.tree.expression.QueryTransformer;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.rag.Query;
import org.springframework.ai.rag.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.rag.generation.augmentation.ContextualQueryAugmenter;
import org.springframework.ai.rag.preretrieval.query.transformation.RewriteQueryTransformer;
import org.springframework.ai.rag.retrieval.join.ConcatenationDocumentJoiner;
import org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v1/ai")
@Tag(name = "chat ai")
public class ChatController {
    private final ChatClient chatClient;
    private final ChatMemory chatMemory;
    private final VectorStore vectorStore;

    public ChatController(ChatClient chatClient, ChatMemory chatMemory, VectorStore vectorStore) {
        this.chatClient = chatClient;
        this.chatMemory = chatMemory;
        this.vectorStore = vectorStore;
    }

    @GetMapping("/stream")
    public ResponseEntity<Flux<String>> generateStream(@RequestParam(value = "message") String message){
        try {
            QuestionAnswerAdvisor qa=QuestionAnswerAdvisor.builder(vectorStore)
                    .searchRequest(SearchRequest.builder().similarityThreshold(0.55d).topK(20).build()).build();

            Query query = new Query(message);

            //QueryTransformer bedzie dzialal dla zapytan o dlugiej tresci i zawilosci. Dla prostych nie jest to dobre rozwiazanie
            RewriteQueryTransformer queryTransformer = RewriteQueryTransformer.builder()
                    .chatClientBuilder(chatClient.mutate())
                    .build();
//            Advisor retrievalAugmentationAdvisor = RetrievalAugmentationAdvisor.builder()
//                    //.queryTransformers(queryTransformer)
//                    .documentRetriever(VectorStoreDocumentRetriever.builder()
//                            .similarityThreshold(0.55)
//                            .vectorStore(vectorStore)
//                            .build())
//                    .queryAugmenter(ContextualQueryAugmenter.builder()
//                            .allowEmptyContext(true)
//                            .build())
//                    .build();
            Flux<String> response = chatClient.prompt()
                    .advisors(new SimpleLoggerAdvisor())
                    .advisors(qa)
//                    .user(queryTransformer.transform(query).text())
                    .user(message)
                    .stream()
                    .content();
            return ResponseEntity.ok(response);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(Flux.just("Something went wrong"));
        }
    }
    @GetMapping("/history")
    public List<Message> getHistory(){
        return chatMemory.get("default");
    }

    @DeleteMapping("history")
    public ResponseEntity<String> deleteHistory(){
        chatMemory.clear("default");
        return ResponseEntity.ok("Deleted history");
    }
}
