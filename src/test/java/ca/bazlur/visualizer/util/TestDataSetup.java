package ca.bazlur.visualizer.util;

import ca.bazlur.visualizer.domain.AbuseConfidenceScore;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class TestDataSetup {

    public static List<AbuseConfidenceScore> prepareTestData() throws IOException, URISyntaxException {
        var json = Files.readString(getFileFromResource("data/abuse_confidence_score.json"));
        var dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ssX");
        return JsonHelper.fromJsonWithOffsetDateTimeAndDateTimeFormatter(new ObjectMapper(), dateTimeFormatter, json, new TypeReference<List<AbuseConfidenceScore>>() {
        });
    }

    private static Path getFileFromResource(String fileName) throws URISyntaxException {
        var classLoader = TestDataSetup.class.getClassLoader();
        var resource = classLoader.getResource(fileName);
        if (resource == null) {
            throw new IllegalArgumentException("file not found! " + fileName);
        } else {
            return Path.of(resource.toURI());
        }
    }
}
