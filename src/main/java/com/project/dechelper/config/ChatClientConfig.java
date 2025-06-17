package com.project.dechelper.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatClientConfig {


    @Bean
    public ChatClient chatClient(ChatClient.Builder builder, ChatMemory chatMemory) {
        return builder
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
                .defaultSystem("You are helpful chat bot that answers questions based on data retrieved from RAG system. You have in disposal tools for RAG and other current information. Answer questions by providing data from tools and reason about them.")
                //.defaultOptions(ChatOptions.builder().temperature(0d).build())
                //ustawienie temperatury na 0 wylacza wywolywanie tooli przez model
                .build();
    }

    @Bean
    public ChatMemory chatMemory(){
        return MessageWindowChatMemory.builder().build();
    }

}
