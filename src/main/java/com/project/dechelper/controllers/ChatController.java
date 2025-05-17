package com.project.dechelper.controllers;

import com.project.dechelper.model.Information;
import com.project.dechelper.model.SentenceDTO;
import com.project.dechelper.services.AiSearchService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v1/ai")
@Tag(name = "chat ai")
public class ChatController {
    private final ChatClient chatClient;

    @Autowired
    private final VectorStore vectorStore;
    private final AiSearchService aiSearchService;

    public ChatController(ChatClient.Builder builder, VectorStore vectorStore, AiSearchService aiSearchService) {
        this.chatClient = builder
                .build();
        this.vectorStore = vectorStore;
        this.aiSearchService = aiSearchService;
    }



    @GetMapping("/generate")
    public ResponseEntity<String> generate(@RequestParam(value = "message") String message){
        try {
            List<Information> relevantData = aiSearchService.getRelevantData(SentenceDTO.builder().sentence(message).returnInfoAmount(2).build());
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
            //TODO caly proces analizy danych przez model jest do przepracowania. Raz na 5 razy uzywa kontekstu i przewaznie nie poprawnie.
            // moze za slaby model, za mala ilosc rekordow, za malo informacji w danych
            String response = chatClient.prompt()
//                    .advisors(new QuestionAnswerAdvisor(vectorStore))
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
}
