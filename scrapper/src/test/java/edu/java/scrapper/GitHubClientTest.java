package edu.java.scrapper;

import com.github.tomakehurst.wiremock.WireMockServer;
import edu.java.client.GitHubClient;
import edu.java.dto.GitHubRepositoryResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.OffsetDateTime;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class GitHubClientTest {
    private WireMockServer wireMockServer;
    private GitHubClient gitHubClient;

    private static final String CONTENT_TYPE = "Content-Type";
    private static final String APPLICATION_JSON = "application/json";
    private static final String REPO_OWNER = "owner";
    private static final String REPO_NAME = "repo";
    private static final String REPO_OWNER1 = "owner1";
    private static final String REPO_NAME1 = "repo1";
    private static final String CREATED_AT = "2010-12-08T04:04:45Z";
    private static final String UPDATED_AT = "2024-02-20T18:42:23Z";
    private static final String PUSHED_AT = "2024-02-20T15:24:24Z";
    private static final String RESPONSE_BODY = String.format(
        "{\"name\": \"%s\", \"owner\": {\"login\": \"%s\"}, \"created_at\": \"%s\", \"updated_at\": \"%s\", \"pushed_at\": \"%s\"}",
        REPO_NAME, REPO_OWNER, CREATED_AT, UPDATED_AT, PUSHED_AT);
    private static final String EMPTY_BODY = "";

    @BeforeEach
    void setUp() {
        wireMockServer = new WireMockServer();
        wireMockServer.start();

        configureStub(REPO_OWNER, REPO_NAME, RESPONSE_BODY);
        configureStub(REPO_OWNER1, REPO_NAME1, EMPTY_BODY);

        gitHubClient = new GitHubClient(WebClient.builder(), wireMockServer.baseUrl());
    }

    private void configureStub(String owner, String repo, String responseBody) {
        wireMockServer.stubFor(get(urlPathEqualTo(String.format("/repos/%s/%s", owner, repo)))
            .willReturn(aResponse()
                .withHeader(CONTENT_TYPE, APPLICATION_JSON)
                .withBody(responseBody)));
    }

    @AfterEach
    void tearDown() {
        wireMockServer.stop();
    }

    @Test
    void fetchRepositoryInfoTest() {
        Mono<GitHubRepositoryResponse> responseMono = gitHubClient.fetchRepositoryInfo(REPO_OWNER, REPO_NAME);
        GitHubRepositoryResponse response = responseMono.block();
        Assertions.assertNotNull(response);
        Assertions.assertEquals(REPO_NAME, response.name());
        Assertions.assertEquals(OffsetDateTime.parse(CREATED_AT), response.createdTime());
        Assertions.assertEquals(OffsetDateTime.parse(UPDATED_AT), response.updateTime());
        Assertions.assertEquals(OffsetDateTime.parse(PUSHED_AT), response.pushedTime());
        Assertions.assertEquals(REPO_OWNER, response.owner().name());
    }

    @Test
    void fetchRepositoryNullInfoTest() {
        Mono<GitHubRepositoryResponse> responseMono = gitHubClient.fetchRepositoryInfo(REPO_OWNER1, REPO_NAME1);
        GitHubRepositoryResponse response = responseMono.block();
        Assertions.assertNull(response);
    }
}
