package edu.java.bot.client;

import dto.AddLinkRequest;
import dto.ApiResponse;
import dto.RemoveLinkRequest;
import edu.java.bot.dto.ClientResponse;
import edu.java.bot.dto.LinkUrlResponse;
import edu.java.bot.dto.ListLinkUrlsResponse;
import org.springframework.http.HttpMethod;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class ScrapperClient {
    private final WebClient webClient;
    private static final String BASE_URL = "http://localhost:8080";
    private static final String LINKS_URI = "/links";
    private static final String TG_CHAT_ID_HEADER = "Tg-Chat-Id";
    private static final String TG_CHAT_URI_TEMPLATE = "/tg-chat/{id}";
    private static final String SUCCESS_MESSAGE = "Link removed successfully";
    private static final String ERROR_MESSAGE = "Error occurred";

    public ScrapperClient(WebClient.Builder webClientBuilder, String baseUrl) {
        this.webClient = webClientBuilder.baseUrl(baseUrl).build();
    }

    public ScrapperClient(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl(BASE_URL).build();
    }

    public Mono<ClientResponse> getAllLinks(Long tgChatId) {
        return webClient.get()
            .uri(LINKS_URI)
            .header(TG_CHAT_ID_HEADER, tgChatId.toString())
            .exchangeToMono(response -> {
                if (response.statusCode().is2xxSuccessful()) {
                    return response.bodyToMono(ListLinkUrlsResponse.class)
                        .map(body -> new ClientResponse(true, SUCCESS_MESSAGE, body));
                } else {
                    return response.bodyToMono(ApiResponse.class)
                        .map(errorBody -> new ClientResponse(false, ERROR_MESSAGE, errorBody));
                }
            });
    }

    public Mono<ClientResponse> addLink(Long tgChatId, AddLinkRequest addLinkRequest) {
        return webClient.post()
            .uri(LINKS_URI)
            .header(TG_CHAT_ID_HEADER, tgChatId.toString())
            .bodyValue(addLinkRequest)
            .exchangeToMono(response -> {
                if (response.statusCode().is2xxSuccessful()) {
                    return response.bodyToMono(LinkUrlResponse.class)
                        .map(body -> new ClientResponse(true, SUCCESS_MESSAGE, body));
                } else {
                    return response.bodyToMono(ApiResponse.class)
                        .map(errorBody -> new ClientResponse(false, ERROR_MESSAGE, errorBody));
                }
            });
    }

    public Mono<ClientResponse> removeLink(Long tgChatId, RemoveLinkRequest removeLinkRequest) {
        return webClient
            .method(HttpMethod.DELETE)
            .uri(LINKS_URI)
            .header(TG_CHAT_ID_HEADER, tgChatId.toString())
            .body(BodyInserters.fromValue(removeLinkRequest))
            .exchangeToMono(response -> {
                if (response.statusCode().is2xxSuccessful()) {
                    return response.bodyToMono(LinkUrlResponse.class)
                        .map(body -> new ClientResponse(true, SUCCESS_MESSAGE, body));
                } else {
                    return response.bodyToMono(ApiResponse.class)
                        .map(errorBody -> new ClientResponse(false, ERROR_MESSAGE, errorBody));
                }
            });
    }

    public Mono<ApiResponse> registerChat(Long id) {
        return webClient.post()
            .uri(TG_CHAT_URI_TEMPLATE, id)
            .retrieve()
            .bodyToMono(ApiResponse.class);
    }

    public Mono<ApiResponse> deleteChat(Long id) {
        return webClient.delete()
            .uri(TG_CHAT_URI_TEMPLATE, id)
            .retrieve()
            .bodyToMono(ApiResponse.class);
    }
}
