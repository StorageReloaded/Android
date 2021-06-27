package io.github.storagereloaded.android.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import io.github.storagereloaded.android.StoReApp;
import io.github.storagereloaded.api.AuthResponse;
import io.github.storagereloaded.api.InfoResponse;
import io.github.storagereloaded.api.StoRe;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginViewModel extends AndroidViewModel {

    StoRe stoRe;

    public LoginViewModel(@NonNull Application application) {
        super(application);
        stoRe = ((StoReApp) application).getStoRe();
    }

    public void setSessionId(String sessionId) {
        stoRe.setSessionId(sessionId);
    }

    public LiveData<Boolean> checkServer(String serverAddress) {
        MutableLiveData<Boolean> data = new MutableLiveData<Boolean>() {
        };

        Call<InfoResponse> response = stoRe.info(serverAddress);
        response.enqueue(new Callback<InfoResponse>() {
            @Override
            public void onResponse(Call<InfoResponse> call, Response<InfoResponse> response) {
                data.postValue(true);
            }

            @Override
            public void onFailure(Call<InfoResponse> call, Throwable t) {
                data.postValue(false);
            }
        });

        return data;
    }

    public LiveData<String> auth(String serverAddress, String username, String password) {
        MutableLiveData<String> data = new MutableLiveData<String>() {
        };

        Call<AuthResponse> response = stoRe.auth(serverAddress, username, password);
        response.enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                if (response.body() == null)
                    data.postValue(null);
                else
                    data.postValue(response.body().getSessionId());
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                data.postValue(null);
            }
        });

        return data;
    }
}
