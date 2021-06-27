package io.github.storagereloaded.api;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface StoReService {
    @GET("info")
    Call<InfoResponse> info();

    @POST("v1/auth")
    Call<AuthResponse> auth(@Body AuthRequest authRequest);

    @GET("v1/databases")
    Call<List<Database>> getDatabases();

    @GET("v1/items")
    Call<List<Item>> getItems();

    @GET("v1/tags")
    Call<List<Tag>> getTags();
}
