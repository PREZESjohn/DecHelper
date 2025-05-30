package com.project.dechelper.controllers;

import com.project.dechelper.model.Information;
import com.project.dechelper.model.SentenceDTO;
import com.project.dechelper.services.AiSearchService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.Message;
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
    @Autowired
    VectorStore vectorStore;

    public ChatController(ChatClient chatClient, ChatMemory chatMemory) {
        this.chatClient = chatClient;
        this.chatMemory = chatMemory;
    }

    @GetMapping("/stream")
    public ResponseEntity<Flux<String>> generateStream(@RequestParam(value = "message") String message){
        try {
            QuestionAnswerAdvisor qa=QuestionAnswerAdvisor.builder(vectorStore)
                    .searchRequest(SearchRequest.builder().similarityThreshold(0.4d).build()).build();

            Flux<String> response = chatClient.prompt()
                    .advisors(qa)
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
        return chatMemory.get("default", 20);
    }

    @DeleteMapping("history")
    public ResponseEntity<String> deleteHistory(){
        chatMemory.clear("default");
        return ResponseEntity.ok("Deleted history");
    }
}
