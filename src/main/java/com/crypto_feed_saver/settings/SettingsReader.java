package com.crypto_feed_saver.settings;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Paths;

public class SettingsReader {
    private static ObjectMapper mapper;

    static {
        mapper = new ObjectMapper(new YAMLFactory());

        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);
        mapper.configure(DeserializationFeature.FAIL_ON_MISSING_CREATOR_PROPERTIES, true);
        mapper.configure(DeserializationFeature.FAIL_ON_NULL_CREATOR_PROPERTIES, true);
    }

    public static <T> T read(String fileName, Class<T> type) throws IOException {
        URI uri = Paths.get(fileName).toAbsolutePath().toUri();
        return mapper.readValue(new File(uri), type);
    }
}
