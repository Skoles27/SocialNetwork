package com.skoles.socialNetwork.payload.response;

import lombok.Getter;

@Getter
public class InvalidSignInResponse {
    private String username;
    private String password;

    public InvalidSignInResponse() {
        this.username = "Invalid Username";
        this.password = "Invalid Password";
    }
}
