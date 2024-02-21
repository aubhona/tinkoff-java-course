package edu.java.scrapper;

import com.github.tomakehurst.wiremock.WireMockServer;
import edu.java.clients.GitHubClient;
import edu.java.dtos.GitHubRepositoryResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import java.time.OffsetDateTime;
import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class GitHubClientTest {
    WireMockServer wireMockServer;
    GitHubClient gitHubClient;

    @BeforeEach
    void setUp() {
        wireMockServer = new WireMockServer();
        wireMockServer.start();

        wireMockServer.stubFor(get(urlPathEqualTo("/repos/owner/repo"))
            .willReturn(aResponse()
                .withHeader("Content-Type", "application/json")
                .withBody(
                    "{\"name\": \"repo\", " +
                    "\"owner\": " +
                    "{" +
                    "\"login\": \"owner\"" +
                    "}, " +
                    "\"created_at\": \"2010-12-08T04:04:45Z\", " +
                    "\"updated_at\": \"2024-02-20T18:42:23Z\", " +
                    "\"pushed_at\": \"2024-02-20T15:24:24Z\"}")));
        wireMockServer.stubFor(get(urlPathEqualTo("/repos/owner1/repo1"))
            .willReturn(aResponse()
                .withHeader("Content-Type", "application/json")
                .withBody(
                    "")));

        gitHubClient = new GitHubClient(WebClient.builder(), wireMockServer.baseUrl());
    }

    @AfterEach
    void tearDown() {
        wireMockServer.stop();
    }

    @Test
    void fetchRepositoryInfoTest() {
        Mono<GitHubRepositoryResponse> responseMono = gitHubClient.fetchRepositoryInfo("owner", "repo");
        GitHubRepositoryResponse response = responseMono.block();
        Assertions.assertNotNull(response);
        Assertions.assertEquals("repo", response.name());
        Assertions.assertEquals(OffsetDateTime.parse("2010-12-08T04:04:45Z"), response.createdTime());
        Assertions.assertEquals(OffsetDateTime.parse("2024-02-20T18:42:23Z"), response.updateTime());
        Assertions.assertEquals(OffsetDateTime.parse("2024-02-20T15:24:24Z"), response.pushedTime());
        Assertions.assertEquals("owner", response.owner().name());
    }

    @Test
    void fetchRepositoryNullInfoTest() {
        Mono<GitHubRepositoryResponse> responseMono = gitHubClient.fetchRepositoryInfo("owner1", "repo1");
        GitHubRepositoryResponse response = responseMono.block();
        Assertions.assertNull(response);
    }
}
