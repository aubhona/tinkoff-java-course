package dto;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class ListLinkUrlsResponse {
    private List<LinkUrlResponse> links;
    private Integer size;
}
