package io.github.storagereloaded.api;

public class AuthRequest {

    String username;
    String password;

    public AuthRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

}
