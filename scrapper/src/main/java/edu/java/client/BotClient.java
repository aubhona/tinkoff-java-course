package edu.java.client;

import dto.ApiResponse;
import dto.LinkUpdateRequest;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class BotClient {
    private final WebClient webClient;
    private static final String BASE_URL = "http://localhost:8080";

    public BotClient(WebClient.Builder webClientBuilder, String baseUrl) {
        this.webClient = webClientBuilder.baseUrl(baseUrl).build();
    }

    public BotClient(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl(BASE_URL).build();
    }

    public Mono<ApiResponse> sendUpdate(LinkUpdateRequest request) {
        return webClient.post()
            .uri("/updates")
            .bodyValue(request)
            .retrieve()
            .bodyToMono(ApiResponse.class);
    }
}
