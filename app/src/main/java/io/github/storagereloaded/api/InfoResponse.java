package io.github.storagereloaded.api;

import com.google.gson.annotations.SerializedName;

public class InfoResponse {
    @SerializedName("api_version")
    int apiVersion;

    @SerializedName("server_version")
    String serverVersion;

    @SerializedName("os")
    String os;

    @SerializedName("os_version")
    String osVersion;
}
