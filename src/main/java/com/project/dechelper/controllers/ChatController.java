package com.project.dechelper.controllers;

import com.project.dechelper.toolCalling.DataRetrievalTools;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.rag.preretrieval.query.transformation.RewriteQueryTransformer;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.util.List;

@RestController
@RequestMapping("api/v1/ai")
@Tag(name = "chat ai")
public class ChatController {
    private final ChatClient chatClient;
    private final ChatMemory chatMemory;
    private final VectorStore vectorStore;
    private final DataRetrievalTools dataRetrievalTools;


    public ChatController(ChatClient chatClient, ChatMemory chatMemory, VectorStore vectorStore, DataRetrievalTools dataRetrievalTools) {
        this.chatClient = chatClient;
        this.chatMemory = chatMemory;
        this.vectorStore = vectorStore;
        this.dataRetrievalTools = dataRetrievalTools;
    }

    @GetMapping("/stream")
    public ResponseEntity<Flux<String>> generateStream(@RequestParam(value = "message") String message){
        try {
            QuestionAnswerAdvisor qa=QuestionAnswerAdvisor.builder(vectorStore)
                    .searchRequest(SearchRequest.builder().similarityThreshold(0.55d).topK(20).build()).build();


            //QueryTransformer bedzie dzialal dla zapytan o dlugiej tresci i zawilosci. Dla prostych nie jest to dobre rozwiazanie
            RewriteQueryTransformer queryTransformer = RewriteQueryTransformer.builder()
                    .chatClientBuilder(chatClient.mutate())
                    .build();
            Flux<String> response = chatClient.prompt()
                    .advisors(new SimpleLoggerAdvisor())
                    .user(message)
//                    .advisors(qa)
                    .tools(dataRetrievalTools)
                    .stream()
                    .content();
            return ResponseEntity.ok(response);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(Flux.just("Something went wrong"));
        }
    }
    @GetMapping("/test")
    public ResponseEntity<String> generateTestWithToolCall(@RequestParam(value = "message") String message){
        try {
            String response = chatClient.prompt()
                    .advisors(new SimpleLoggerAdvisor())
                    .user(message)
                    .tools(dataRetrievalTools)
                    .call()
                    .content();
            return ResponseEntity.ok(response);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Something went wrong");
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
