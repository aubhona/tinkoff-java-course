package edu.java.bot.client;

import dto.AddLinkRequest;
import dto.LinkResponse;
import dto.ListLinksResponse;
import dto.RemoveLinkRequest;
import org.springframework.http.HttpMethod;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class ScrapperClient {
    private final WebClient webClient;
    private static final String BASE_URL = "http://localhost:8080";

    public ScrapperClient(WebClient.Builder webClientBuilder, String baseUrl) {
        this.webClient = webClientBuilder.baseUrl(baseUrl).build();
    }

    public ScrapperClient(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl(BASE_URL).build();
    }

    public Mono<ListLinksResponse> getAllLinks(Long tgChatId) {
        return webClient.get()
            .uri("/links")
            .header("Tg-Chat-Id", tgChatId.toString())
            .retrieve()
            .bodyToMono(ListLinksResponse.class);
    }

    public Mono<LinkResponse> addLink(Long tgChatId, AddLinkRequest addLinkRequest) {
        return webClient.post()
            .uri("/links")
            .header("Tg-Chat-Id", tgChatId.toString())
            .bodyValue(addLinkRequest)
            .retrieve()
            .bodyToMono(LinkResponse.class);
    }

    public Mono<LinkResponse> removeLink(Long tgChatId, RemoveLinkRequest removeLinkRequest) {
        return webClient
            .method(HttpMethod.DELETE)
            .uri("/links")
            .header("Tg-Chat-Id", tgChatId.toString())
            .body(BodyInserters.fromValue(removeLinkRequest))
            .retrieve()
            .bodyToMono(LinkResponse.class);
    }

    public Mono<String> registerChat(Long id) {
        return webClient.post()
            .uri("/tg-chat/{id}", id)
            .retrieve()
            .bodyToMono(String.class);
    }

    public Mono<String> deleteChat(Long id) {
        return webClient.delete()
            .uri("/tg-chat/{id}", id)
            .retrieve()
            .bodyToMono(String.class);
    }
}
