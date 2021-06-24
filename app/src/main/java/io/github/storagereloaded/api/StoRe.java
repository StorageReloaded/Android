package io.github.storagereloaded.api;

import java.util.Arrays;
import java.util.List;

import io.github.storagereloaded.api.impl.DatabaseDummyImpl;
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

    public List<Database> getDatabases() {
        return Arrays.asList(new DatabaseDummyImpl());
    }

    public boolean sync(List<Database> databases) {
        return false;
    }

    private String getServerUrl() {
        if (!serverAddress.startsWith("http://") && !serverAddress.startsWith("https://"))
            return "https://" + serverAddress + "/api/";

        return serverAddress + "/api/";
    }

    private void createService() {
        service = new Retrofit.Builder()
                .baseUrl(getServerUrl())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(StoReService.class);
    }
}
