package dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.net.URI;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LinkUrlResponse {
    private Long id;
    private URI url;

    @JsonCreator
    public LinkUrlResponse(
        @JsonProperty("id") Long id,
        @JsonProperty("url") String urlString) {
        this.id = id;
        this.url = URI.create(urlString);
    }
}
