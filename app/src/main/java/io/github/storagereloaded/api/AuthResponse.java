package io.github.storagereloaded.api;

import com.google.gson.annotations.SerializedName;

public class AuthResponse {
    @SerializedName("session_id")
    String sessionId;

    public String getSessionId() {
        return sessionId;
    }
}
