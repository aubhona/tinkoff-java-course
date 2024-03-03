package edu.java.scrapper;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import dto.LinkUpdateRequest;
import edu.java.client.BotClient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.test.StepVerifier;

import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

class BotClientTest {

    private WireMockServer wireMockServer;
    private BotClient botClient;

    private static final int WIREMOCK_PORT = 8080;
    private static final String HOST = "localhost";
    private static final String BASE_URL_FORMAT = "http://%s:%d";
    private static final String UPDATES_ENDPOINT = "/updates";
    private static final String CONTENT_TYPE_HEADER = "Content-Type";
    private static final String APPLICATION_JSON = "application/json";
    private static final String SUCCESS_DESCRIPTION = "Successfully processed";
    private static final String SUCCESS_RESPONSE_BODY_FORMAT = "{\"description\":\"%s\"}";
    private static final int OK_STATUS = 200;
    private static final long TEST_ID = 1L;
    private static final String TEST_URL = "http://example.com";
    private static final String TEST_DESCRIPTION = "Test update";
    private static final long TEST_CHAT_ID = 12345L;

    @BeforeEach
    void setUp() {
        wireMockServer = new WireMockServer(WIREMOCK_PORT);
        wireMockServer.start();
        WireMock.configureFor(HOST, wireMockServer.port());
        WebClient.Builder webClientBuilder = WebClient.builder();
        botClient = new BotClient(webClientBuilder, String.format(BASE_URL_FORMAT, HOST, WIREMOCK_PORT));
    }

    @AfterEach
    void tearDown() {
        wireMockServer.stop();
    }

    @Test
    void sendUpdate_success() {
        String successResponseBody = String.format(SUCCESS_RESPONSE_BODY_FORMAT, SUCCESS_DESCRIPTION);

        stubFor(post(urlEqualTo(UPDATES_ENDPOINT))
            .willReturn(aResponse()
                .withHeader(CONTENT_TYPE_HEADER, APPLICATION_JSON)
                .withStatus(OK_STATUS)
                .withBody(successResponseBody)));

        LinkUpdateRequest request = new LinkUpdateRequest();
        request.setId(TEST_ID);
        request.setUrl(TEST_URL);
        request.setDescription(TEST_DESCRIPTION);
        request.setTgChatIds(List.of(TEST_CHAT_ID));

        StepVerifier.create(botClient.sendUpdate(request))
            .expectNextMatches(response -> SUCCESS_DESCRIPTION.equals(response.getDescription()))
            .verifyComplete();
    }
}
