package dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.Getter;
import lombok.Setter;



@Getter
@Setter
public class LinkUpdateRequest {

    @NotNull(message = "ID cannot be null")
    private Long id;

    @NotNull(message = "URL cannot be null")
    @Pattern(regexp = "^(http|https)://.*", message = "URL must be a valid web address")
    private String url;

    @Size(max = 500, message = "Description cannot be longer than 500 characters")
    private String description;

    @Size(min = 1, message = "List of chats cannot be empty")
    private List<Long> tgChatIds;
}
