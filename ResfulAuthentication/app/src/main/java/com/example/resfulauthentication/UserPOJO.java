package com.example.resfulauthentication;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserPOJO {

    public List<User> getUsers() {
        return users;
    }

    @SerializedName("users")
    @Expose
    private List<User> users;

}
