package edu.java.bot.service.parsers;

import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
public class LinkParser {
    private static final int OK_CODE_MAX = 399;
    private static final int OK_CODE_MIN = 200;

    @Setter
    private URI uri;
    private final List<String> validHosts;

    public static Map.Entry<Boolean, URI> tryParse(String urlString) {
        try {
            URI uri = new URI(urlString);
            return new AbstractMap.SimpleEntry<>(isLinkWorking(uri.toURL()), uri);
        } catch (Exception e) {
            return new AbstractMap.SimpleEntry<>(false, null);
        }
    }

    public static boolean isLinkWorking(URL url) {
        try {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("HEAD");
            connection.connect();
            int responseCode = connection.getResponseCode();
            return (OK_CODE_MIN <= responseCode && responseCode <= OK_CODE_MAX);
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isValidHost() {
        for (String validHost : validHosts) {
            if (uri.getHost().endsWith(validHost)) {
                return true;
            }
        }

        return false;
    }
}
