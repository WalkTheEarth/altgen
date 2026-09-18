package com.altgen;

import com.google.gson.Gson;
import meteordevelopment.meteorclient.utils.network.Http;
import org.jetbrains.annotations.Nullable;

/**
 * Client for TheAltening's API v2, see https://thealtening.com/api
 */
public class AlteningApi {
    private static final Gson GSON = new Gson();

    public static final String BASE = "https://api.thealtening.com/v2";

    /**
     * Generates a fresh alt token.
     * <p>Blocking - call from a background thread.</p>
     */
    public static @Nullable Result generate(String apiKey) {
        try {
            String body = Http.get(BASE + "/generate?key=" + apiKey + "&info=true").sendString();
            if (body == null) return null;

            return GSON.fromJson(body, Result.class);
        } catch (Exception e) {
            AltGen.LOG.error("Failed to generate an Altening token", e);
            return null;
        }
    }

    public static class Result {
        public String token;
        public @Nullable String username;
        public boolean limit;
        public @Nullable String skin;
        public @Nullable Info info;

        public static class Info {
        }
    }
}
