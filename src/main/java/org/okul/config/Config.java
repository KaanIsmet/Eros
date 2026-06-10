package org.okul.config;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class Config {
    private static final Map<String, String> dotenv = loadDotenv();

    private Config() {}

    public static void load() {
        // Triggers class initialization and .env loading.
    }

    public static String get(String key) {
        String value = System.getenv(key);
        if (value != null) {
            return value;
        }
        return dotenv.get(key);
    }

    public static String getOrDefault(String key, String defaultValue) {
        String value = get(key);
        return value != null ? value : defaultValue;
    }

    private static Map<String, String> loadDotenv() {
        for (Path envPath : envFileCandidates()) {
            Map<String, String> parsed = parseEnvFile(envPath);
            if (!parsed.isEmpty()) {
                return parsed;
            }
        }
        return Map.of();
    }

    private static List<Path> envFileCandidates() {
        List<Path> candidates = new ArrayList<>();
        candidates.add(Path.of(".env"));

        try {
            Path codeLocation = Path.of(
                    Config.class.getProtectionDomain().getCodeSource().getLocation().toURI()
            );
            if (Files.isRegularFile(codeLocation)) {
                Path jarDir = codeLocation.getParent();
                if (jarDir != null) {
                    candidates.add(jarDir.resolve(".env"));
                    Path projectRoot = jarDir.getParent();
                    if (projectRoot != null) {
                        candidates.add(projectRoot.resolve(".env"));
                    }
                }
            } else if (Files.isDirectory(codeLocation)) {
                candidates.add(codeLocation.resolve(".env"));
                Path moduleRoot = codeLocation.getParent();
                if (moduleRoot != null) {
                    candidates.add(moduleRoot.resolve(".env"));
                }
            }
        } catch (URISyntaxException | SecurityException ignored) {
            // Fall back to working-directory lookup only.
        }

        return candidates;
    }

    private static Map<String, String> parseEnvFile(Path envPath) {
        Map<String, String> map = new HashMap<>();
        if (!Files.isRegularFile(envPath)) {
            return map;
        }
        try {
            for (String line : Files.readAllLines(envPath)) {
                line = line.strip();
                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }
                int eq = line.indexOf('=');
                if (eq <= 0) {
                    continue;
                }
                String key = line.substring(0, eq).strip();
                String value = line.substring(eq + 1).strip();
                map.put(key, value);
            }
        } catch (IOException ignored) {
            // .env is optional
        }
        return map;
    }
}
