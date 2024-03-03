package edu.java.bot.client;

import dto.AddLinkRequest;
import dto.ApiResponse;
import dto.LinkResponse;
import dto.LinkUrlResponse;
import dto.ListLinkUrlsResponse;
import edu.java.bot.dto.ClientResponse;
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

    public Mono<ClientResponse> getAllLinks(Long tgChatId) {
        return webClient.get()
            .uri("/links")
            .header("Tg-Chat-Id", tgChatId.toString())
            .exchangeToMono(response -> {
                    if (response.statusCode().is2xxSuccessful()) {
                        return response.bodyToMono(ListLinkUrlsResponse.class)
                            .map(body -> new ClientResponse(true, "Link removed successfully", body));
                    } else {
                        return response.bodyToMono(ApiResponse.class)
                            .map(errorBody -> new ClientResponse(false, "Error occurred", errorBody));
                    }
                }
            );
    }

    public Mono<ClientResponse> addLink(Long tgChatId, AddLinkRequest addLinkRequest) {
        return webClient.post()
            .uri("/links")
            .header("Tg-Chat-Id", tgChatId.toString())
            .bodyValue(addLinkRequest)
            .exchangeToMono(response -> {
                if (response.statusCode().is2xxSuccessful()) {
                    return response.bodyToMono(LinkUrlResponse.class)
                        .map(body -> new ClientResponse(true, "Link removed successfully", body));
                } else {
                    return response.bodyToMono(ApiResponse.class)
                        .map(errorBody -> new ClientResponse(false, "Error occurred", errorBody));
                }
            });
    }

    public Mono<ClientResponse> removeLink(Long tgChatId, RemoveLinkRequest removeLinkRequest) {
        return webClient
            .method(HttpMethod.DELETE)
            .uri("/links")
            .header("Tg-Chat-Id", tgChatId.toString())
            .body(BodyInserters.fromValue(removeLinkRequest))
            .exchangeToMono(response -> {
                if (response.statusCode().is2xxSuccessful()) {
                    return response.bodyToMono(LinkUrlResponse.class)
                        .map(body -> new ClientResponse(true, "Link removed successfully", body));
                } else {
                    return response.bodyToMono(ApiResponse.class)
                        .map(errorBody -> new ClientResponse(false, "Error occurred", errorBody));
                }
            });
    }

    public Mono<ApiResponse> registerChat(Long id) {
        return webClient.post()
            .uri("/tg-chat/{id}", id)
            .retrieve()
            .bodyToMono(ApiResponse.class);
    }

    public Mono<ApiResponse> deleteChat(Long id) {
        return webClient.delete()
            .uri("/tg-chat/{id}", id)
            .retrieve()
            .bodyToMono(ApiResponse.class);
    }
}
