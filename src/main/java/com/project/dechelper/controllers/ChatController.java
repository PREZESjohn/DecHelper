package com.project.dechelper.controllers;

import com.project.dechelper.advisors.Qwen3ThinkFilterAdvisor;
import com.project.dechelper.toolCalling.DataModifyTools;
import com.project.dechelper.toolCalling.DataRetrievalTools;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.Message;
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
    private final DataRetrievalTools dataRetrievalTools;
    private final DataModifyTools dataModifyTools;


    public ChatController(ChatClient chatClient, ChatMemory chatMemory, DataRetrievalTools dataRetrievalTools, DataModifyTools dataModifyTools) {
        this.chatClient = chatClient;
        this.chatMemory = chatMemory;
        this.dataRetrievalTools = dataRetrievalTools;
        this.dataModifyTools = dataModifyTools;
    }

    @GetMapping("/stream")
    public ResponseEntity<Flux<String>> generateStream(@RequestParam(value = "message") String message){
        try {
            //TODO When Spring AI will release support for changing thinking mode, Qwen3ThinkFilterAdvisor will not be necessary
            Flux<String> response = chatClient.prompt()
                    .user(message)
                    .advisors(new Qwen3ThinkFilterAdvisor(true))
                    .tools(dataRetrievalTools ,dataModifyTools)
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
                    .user(message)
                    .advisors(new Qwen3ThinkFilterAdvisor(false))
                    .tools(dataRetrievalTools, dataModifyTools)
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
