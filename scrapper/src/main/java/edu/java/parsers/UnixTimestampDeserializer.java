package edu.java.parsers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

public class UnixTimestampDeserializer extends JsonDeserializer<OffsetDateTime> {
    @Override
    public OffsetDateTime deserialize(
        JsonParser parser,
        DeserializationContext deserializationContext
        ) throws IOException {
        return Instant.ofEpochSecond(parser.getLongValue()).atOffset(ZoneOffset.UTC);
    }
}
