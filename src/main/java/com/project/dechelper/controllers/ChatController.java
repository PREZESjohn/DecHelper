package com.project.dechelper.controllers;

import com.project.dechelper.model.Information;
import com.project.dechelper.model.SentenceDTO;
import com.project.dechelper.services.AiSearchService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
    private final AiSearchService aiSearchService;

    //maximum amount of return informations from RAG
    private final Integer infoReturnAmount=10;
    //how much data must be precise to request (lower = more precise)
    private final Double similarityThreshold=0.4;

    public ChatController(ChatClient chatClient, ChatMemory chatMemory, AiSearchService aiSearchService) {
        this.chatClient = chatClient;
        this.chatMemory = chatMemory;
        this.aiSearchService = aiSearchService;
    }

    @GetMapping("/generate")
    public ResponseEntity<String> generate(@RequestParam(value = "message") String message){
        try {
            List<Information> relevantData = aiSearchService.getRelevantData(
                    SentenceDTO.builder().sentence(message).build()
            );
            String relevantDataString = relevantData.stream().map(Information::getContent).collect(Collectors.joining("\n"));
            String combinedMessage = """
                    %s
                    
                    Context information is below.
                    
                    ---------------------
                    %s
                    ---------------------
                    
                    Given the context information, answer the query.
                    
                    Follow these rules:
                    
                    1. If the answer is not in the context, just say that you don't know.
                    2. Avoid statements like "Based on the context..." or "The provided information...".
                    """.formatted(message, relevantDataString);
            String response = chatClient.prompt()
                    .user(combinedMessage)
                    .call()
                    .content()
                    .toString();
            return ResponseEntity.ok(response);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Something went wrong");
        }
    }
    @GetMapping("/stream")
    public ResponseEntity<Flux<String>> generateStream(@RequestParam(value = "message") String message){
        try {
            List<Information> relevantData = aiSearchService.getRelevantData(
                    SentenceDTO.builder()
                            .sentence(message)
                            .similarityThreshold(similarityThreshold)
                            .returnInfoAmount(infoReturnAmount)
                            .build()
            );
            String relevantDataString = relevantData.stream().map(Information::getContent).collect(Collectors.joining("\n"));
            String combinedMessage = """
                    %s
                    
                    Context information is below.
                    
                    ---------------------
                    %s
                    ---------------------
                    
                    Given the context information, answer the query.
                    
                    Follow these rules:
                    
                    1. If the answer is not in the context, just say that you don't know.
                    2. Avoid statements like "Based on the context..." or "The provided information...".
                    """.formatted(message, relevantDataString);
            Flux<String> response = chatClient.prompt()
                    .user(combinedMessage)
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
