package edu.java.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import edu.java.parser.UnixTimestampDeserializer;
import java.time.OffsetDateTime;
import java.util.List;

public record StackOverflowQuestionResponse(@JsonProperty("items") List<ItemResponse> items) {
    public record ItemResponse(
        @JsonProperty("question_id") String questionId,
        @JsonProperty("last_activity_date")
        @JsonDeserialize(using = UnixTimestampDeserializer.class)
        OffsetDateTime lastActivityDate,
        @JsonProperty("creation_date")
        @JsonDeserialize(using = UnixTimestampDeserializer.class)
        OffsetDateTime creationDate,
        @JsonProperty("last_edit_date")
        @JsonDeserialize(using = UnixTimestampDeserializer.class)
        OffsetDateTime lastEditDate,
        @JsonProperty("title") String title) {}
}
