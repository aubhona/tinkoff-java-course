package edu.java.bot.service.parsers;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter @AllArgsConstructor
public enum LinkConstants {
    SUPPORTED_LINKS(new ArrayList<>() {{
        add("https://github.com");
        add("https://stackoverflow.com");
    }});
    private final List<String> supportedLinks;
}
