package com.endryha.aws;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
    private static final Logger logger = LoggerFactory.getLogger(Utils.class);

    private Utils() {
    }

    public static String getFileName(String path) {
        return Paths.get(path).getFileName().toString();
    }

    public static String getExtension(String path) {
        // Infer the image type.
        Matcher matcher = Pattern.compile(".*\\.([^\\.]*)").matcher(path);
        if (!matcher.matches()) {
            logger.info("Unable to infer image type for key {}", path);
            return "";
        }
        return matcher.group(1);
    }

}
