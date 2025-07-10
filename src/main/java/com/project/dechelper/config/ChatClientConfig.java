package com.project.dechelper.config;

import lombok.SneakyThrows;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.model.tool.ToolCallingChatOptions;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.nio.charset.StandardCharsets;

@Configuration
public class ChatClientConfig {

    @Value("classpath:chatSystemConfig.txt")
    private Resource chatSystemConfig;

    @SneakyThrows
    @Bean
    public ChatClient chatClient(ChatClient.Builder builder, ChatMemory chatMemory) {
        return builder
                .defaultAdvisors(new SimpleLoggerAdvisor())
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
                .defaultSystem(chatSystemConfig.getContentAsString(StandardCharsets.UTF_8))
                .defaultOptions(OllamaOptions.builder().numPredict(-1).build())
                //ustawienie temperatury na 0 wylacza wywolywanie tooli przez model
                .build();
    }

    @Bean
    public ChatMemory chatMemory(){
        return MessageWindowChatMemory.builder().build();
    }

}
