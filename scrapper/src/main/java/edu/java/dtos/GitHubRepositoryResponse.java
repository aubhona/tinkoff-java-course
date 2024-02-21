package edu.java.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;

public record GitHubRepositoryResponse(
    @JsonProperty("name") String name,
    @JsonProperty("created_at") OffsetDateTime createdTime,
    @JsonProperty("updated_at") OffsetDateTime updateTime,
    @JsonProperty("pushed_at") OffsetDateTime pushedTime,
    @JsonProperty("owner") Owner owner) {
    public record Owner(@JsonProperty("login") String name) {}
}
