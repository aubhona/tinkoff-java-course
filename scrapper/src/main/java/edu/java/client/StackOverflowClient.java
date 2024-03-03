package edu.java.client;

import edu.java.dto.StackOverflowQuestionResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class StackOverflowClient {
    private final WebClient webClient;
    private final static String BASE_URL = "https://api.stackexchange.com";

    public StackOverflowClient(WebClient.Builder webClientBuilder, String baseUrl) {
        this.webClient = webClientBuilder.baseUrl(baseUrl).build();
    }

    public StackOverflowClient(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl(BASE_URL).build();
    }

    public Mono<StackOverflowQuestionResponse> fetchQuestionInfo(String questionId) {
        return this.webClient.get()
            .uri(uriBuilder -> uriBuilder
                .path("/2.3/questions/{questionId}")
                .queryParam("site", "stackoverflow")
                .build(questionId))
            .retrieve()
            .bodyToMono(StackOverflowQuestionResponse.class);
    }
}

