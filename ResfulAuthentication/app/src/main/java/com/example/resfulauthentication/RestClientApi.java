package com.example.resfulauthentication;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RestClientApi {

    String BASE_URL="https://my-json-server.typicode.com/MeronZerihun/UserJson/";

    @GET("db")
    Call<UserPOJO> getAllUsers();
}
