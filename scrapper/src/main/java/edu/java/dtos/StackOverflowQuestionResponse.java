package edu.java.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;
import java.util.List;

public record StackOverflowQuestionResponse(@JsonProperty("items") List<ItemResponse> items) {
    record ItemResponse(@JsonProperty("question_id") String questionId,
                        @JsonProperty("last_activity_date") OffsetDateTime lastActivityDate,
                        @JsonProperty("creation_date") OffsetDateTime creationDate,
                        @JsonProperty("last_edit_date") OffsetDateTime lastEditDate,
                        @JsonProperty("title") String title) {}
}
