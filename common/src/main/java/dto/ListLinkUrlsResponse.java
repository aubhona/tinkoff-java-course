package dto;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ListLinkUrlsResponse {
    private List<LinkUrlResponse> links;
    private Integer size;
}
