package edu.java.scrapper;

import com.github.tomakehurst.wiremock.WireMockServer;
import edu.java.clients.StackOverflowClient;
import edu.java.dtos.StackOverflowQuestionResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import java.time.Instant;
import java.time.ZoneOffset;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;

public class StackOverflowClientTest {
    WireMockServer wireMockServer;
    StackOverflowClient stackOverflowClient;

    @BeforeEach
    void setUp() {
        wireMockServer = new WireMockServer();
        wireMockServer.start();

        wireMockServer.stubFor(get(urlPathEqualTo("/2.3/questions/11227809"))
            .withQueryParam("site", equalTo("stackoverflow"))
            .willReturn(aResponse()
                .withHeader("Content-Type", "application/json")
                .withBody(
                        """
                                {"items": [
                                {
                                "last_activity_date": 1706021923,
                                "creation_date": 1340805096,
                                "last_edit_date": 1701123268,
                                "question_id": 11227809,
                                "title": "Why is processing a sorted array faster than processing an unsorted array?"
                                }
                                ]}""")));
        wireMockServer.stubFor(get(urlPathEqualTo("/2.3/questions/1"))
            .withQueryParam("site", equalTo("stackoverflow"))
            .willReturn(aResponse()
                .withHeader("Content-Type", "application/json")
                .withBody(
                    """
                            {"items": [
                            {
                            "last_activity_date": 1706021923,
                            "creation_date": 1340805096,
                            "question_id": 1,
                            "title": "Why is processing a sorted array faster than processing an unsorted array?"
                            }
                            ]}""")));

        wireMockServer.stubFor(get(urlPathEqualTo("/2.3/questions/2"))
            .withQueryParam("site", equalTo("stackoverflow"))
            .willReturn(aResponse()
                .withHeader("Content-Type", "application/json")
                .withBody("")));

        stackOverflowClient = new StackOverflowClient(WebClient.builder(), wireMockServer.baseUrl());
    }

    @AfterEach
    void tearDown() {
        wireMockServer.stop();
    }

    @Test
    void fetchQuestionInfoTest() {
        Mono<StackOverflowQuestionResponse> responseMono = stackOverflowClient.fetchQuestionInfo("11227809");
        StackOverflowQuestionResponse response = responseMono.block();
        Assertions.assertNotNull(response);
        Assertions.assertEquals("11227809", response.items().getFirst().questionId());
        Assertions.assertEquals(Instant.ofEpochSecond(1706021923L).atOffset(ZoneOffset.UTC), response.items().getFirst().lastActivityDate());
        Assertions.assertEquals(Instant.ofEpochSecond(1340805096L).atOffset(ZoneOffset.UTC), response.items().getFirst().creationDate());
        Assertions.assertEquals(Instant.ofEpochSecond(1701123268L).atOffset(ZoneOffset.UTC), response.items().getFirst().lastEditDate());
        Assertions.assertEquals("Why is processing a sorted array faster than processing an unsorted array?",
            response.items().getFirst().title());
    }

    @Test
    void fetchQuestionInfoWithNullTest() {
        Mono<StackOverflowQuestionResponse> responseMono = stackOverflowClient.fetchQuestionInfo("1");
        StackOverflowQuestionResponse response = responseMono.block();
        Assertions.assertNotNull(response);
        Assertions.assertEquals("1", response.items().getFirst().questionId());
        Assertions.assertEquals(Instant.ofEpochSecond(1706021923L).atOffset(ZoneOffset.UTC), response.items().getFirst().lastActivityDate());
        Assertions.assertEquals(Instant.ofEpochSecond(1340805096L).atOffset(ZoneOffset.UTC), response.items().getFirst().creationDate());
        Assertions.assertNull(response.items().getFirst().lastEditDate());
        Assertions.assertEquals("Why is processing a sorted array faster than processing an unsorted array?",
            response.items().getFirst().title());
    }
    @Test
    void fetchQuestionNulInfoTest() {
        Mono<StackOverflowQuestionResponse> responseMono = stackOverflowClient.fetchQuestionInfo("2");
        StackOverflowQuestionResponse response = responseMono.block();
        Assertions.assertNull(response);
    }
}
