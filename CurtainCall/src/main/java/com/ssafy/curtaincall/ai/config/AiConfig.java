package com.ssafy.curtaincall.ai.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class AiConfig {
	@Bean
    public ChatClient chatClient(OpenAiChatModel openAiChatModel) {
        return ChatClient.builder(openAiChatModel).build();
    }
	
	// 모든 요청 잡아다가 헤더 강제 삽입, json으로 바꿔줍니다
	@Bean
	public RestClient.Builder restClientBuilder() {
		return RestClient.builder()
				.requestInterceptor((request, body, execution)-> {
					request.getHeaders().set("Content-Type", "application/json");
					
					return execution.execute(request, body);
				});
	}
}
