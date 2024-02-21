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

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class StackOverflowClientTest {
    private WireMockServer wireMockServer;
    private StackOverflowClient stackOverflowClient;

    private static final String CONTENT_TYPE = "Content-Type";
    private static final String APPLICATION_JSON = "application/json";
    private static final String QUESTION_ID_1 = "11227809";
    private static final String QUESTION_ID_2 = "1";
    private static final String QUESTION_ID_3 = "2";
    private static final long LAST_ACTIVITY_DATE = 1706021923L;
    private static final long CREATION_DATE = 1340805096L;
    private static final long LAST_EDIT_DATE = 1701123268L;
    private static final String RESPONSE_BODY_1 = """
            {"items": [
                {
                    "last_activity_date": %d,
                    "creation_date": %d,
                    "last_edit_date": %d,
                    "question_id": 11227809,
                    "title": "Why is processing a sorted array faster than processing an unsorted array?"
                }
            ]}""";
    private static final String RESPONSE_BODY_2 = """
            {"items": [
                {
                    "last_activity_date": %d,
                    "creation_date": %d,
                    "question_id": 1,
                    "title": "Why is processing a sorted array faster than processing an unsorted array?"
                }
            ]}""";
    private static final String EMPTY_BODY = "";

    @BeforeEach
    void setUp() {
        wireMockServer = new WireMockServer();
        wireMockServer.start();

        configureStub(QUESTION_ID_1, String.format(RESPONSE_BODY_1, LAST_ACTIVITY_DATE, CREATION_DATE, LAST_EDIT_DATE));
        configureStub(QUESTION_ID_2, String.format(RESPONSE_BODY_2, LAST_ACTIVITY_DATE, CREATION_DATE));
        configureStub(QUESTION_ID_3, EMPTY_BODY);

        stackOverflowClient = new StackOverflowClient(WebClient.builder(), wireMockServer.baseUrl());
    }

    private void configureStub(String questionId, String responseBody) {
        wireMockServer.stubFor(get(urlPathEqualTo("/2.3/questions/" + questionId))
            .withQueryParam("site", equalTo("stackoverflow"))
            .willReturn(aResponse()
                .withHeader(CONTENT_TYPE, APPLICATION_JSON)
                .withBody(responseBody)));
    }

    @AfterEach
    void tearDown() {
        wireMockServer.stop();
    }

    @Test
    void fetchQuestionInfoTest() {
        Mono<StackOverflowQuestionResponse> responseMono = stackOverflowClient.fetchQuestionInfo(QUESTION_ID_1);
        StackOverflowQuestionResponse response = responseMono.block();
        Assertions.assertNotNull(response);
        Assertions.assertEquals(QUESTION_ID_1, response.items().getFirst().questionId());
        Assertions.assertEquals(Instant.ofEpochSecond(LAST_ACTIVITY_DATE).atOffset(ZoneOffset.UTC), response.items().getFirst().lastActivityDate());
        Assertions.assertEquals(Instant.ofEpochSecond(CREATION_DATE).atOffset(ZoneOffset.UTC), response.items().getFirst().creationDate());
        Assertions.assertEquals(Instant.ofEpochSecond(LAST_EDIT_DATE).atOffset(ZoneOffset.UTC), response.items().getFirst().lastEditDate());
        Assertions.assertEquals("Why is processing a sorted array faster than processing an unsorted array?", response.items().getFirst().title());
    }

    @Test
    void fetchQuestionInfoWithNullTest() {
        Mono<StackOverflowQuestionResponse> responseMono = stackOverflowClient.fetchQuestionInfo(QUESTION_ID_2);
        StackOverflowQuestionResponse response = responseMono.block();
        Assertions.assertNotNull(response);
        Assertions.assertEquals(QUESTION_ID_2, response.items().getFirst().questionId());
        Assertions.assertEquals(Instant.ofEpochSecond(LAST_ACTIVITY_DATE).atOffset(ZoneOffset.UTC), response.items().getFirst().lastActivityDate());
        Assertions.assertEquals(Instant.ofEpochSecond(CREATION_DATE).atOffset(ZoneOffset.UTC), response.items().getFirst().creationDate());
        Assertions.assertNull(response.items().getFirst().lastEditDate());
        Assertions.assertEquals("Why is processing a sorted array faster than processing an unsorted array?", response.items().getFirst().title());
    }

    @Test
    void fetchQuestionNullInfoTest() {
        Mono<StackOverflowQuestionResponse> responseMono = stackOverflowClient.fetchQuestionInfo(QUESTION_ID_3);
        StackOverflowQuestionResponse response = responseMono.block();
        Assertions.assertNull(response);
    }
}
