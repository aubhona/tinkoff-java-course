package edu.java.bot.service.parser;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter @AllArgsConstructor
public enum LinkConstants {
    SUPPORTED_LINKS(new ArrayList<>() {{
        add("github.com");
        add("stackoverflow.com");
    }});
    private final List<String> supportedLinks;
}
