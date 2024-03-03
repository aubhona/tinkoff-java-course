package dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RemoveLinkRequest {
    @NotNull(message = "URL cannot be null")
    @Pattern(regexp = "^(http|https)://.*", message = "URL must be a valid web address")
    private String link;
}
