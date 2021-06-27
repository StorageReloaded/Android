package io.github.storagereloaded.api;

import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class StoRe {

    private String sessionId;
    private String serverAddress;

    private StoReService service;

    public StoRe() {

    }

    public StoRe(String serverAddress, String sessionId) {
        this.serverAddress = serverAddress;
        this.sessionId = sessionId;
        createService();
    }

    public boolean hasLogin() {
        return serverAddress != null && sessionId != null;
    }

    public String getServerAddress() {
        return serverAddress;
    }

    public void setServerAddress(String serverAddress) {
        this.serverAddress = serverAddress;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public Call<InfoResponse> info() {
        return service.info();
    }

    public Call<InfoResponse> info(String serverAddress) {
        setServerAddress(serverAddress);
        createService();
        return service.info();
    }

    public Call<AuthResponse> auth(String serverAddress, String username, String password) {
        setServerAddress(serverAddress);
        createService();
        return service.auth(new AuthRequest(username, password));
    }

    public Call<List<Database>> getDatabases() {
        return service.getDatabases();
    }

    public Call<List<Item>> getItems() {
        return service.getItems();
    }

    public Call<List<Tag>> getTags() {
        return service.getTags();
    }

    private String getServerUrl() {
        if (!serverAddress.startsWith("http://") && !serverAddress.startsWith("https://"))
            return "https://" + serverAddress + "/api/";

        return serverAddress + "/api/";
    }

    private void createService() {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(chain -> {
            Request.Builder builder = chain.request().newBuilder()
                    .header("Content-Type", "application/json")
                    .header("Accept", "application/json");
            if (sessionId != null)
                builder = builder.header("X-StoRe-Session", sessionId);

            return chain.proceed(builder.build());
        });

        service = new Retrofit.Builder()
                .baseUrl(getServerUrl())
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(StoReService.class);
    }
}
