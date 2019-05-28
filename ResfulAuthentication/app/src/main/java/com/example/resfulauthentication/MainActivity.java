package com.example.resfulauthentication;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    EditText emailEditText,passwordEditText;
    Button loginButton;
    HashMap<String, String> hashMap = new HashMap<>();
    boolean loginSuccess = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(!haveNetworkConnection()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Connect to WiFi or quit")
                    .setCancelable(false)
                    .setPositiveButton("Connect to WiFi", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                        }
                    })
                    .setNegativeButton("Quit", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            finish();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }
        else {

            emailEditText = findViewById(R.id.email_edit_text);
            passwordEditText = findViewById(R.id.password_edit_text);
            loginButton = findViewById(R.id.login_button);

            validateClient();

            loginButton.setOnClickListener(new View.OnClickListener() {


                @Override
                public void onClick(View v) {
                    String email = emailEditText.getText().toString();
                    String password = passwordEditText.getText().toString();
                    for (Map.Entry<String, String> entry : hashMap.entrySet()) {
                        Log.d("email", "->" + email + " " + entry.getKey());
                        Log.d("password", "->" + password + " " + entry.getValue());
                        if (entry.getKey().equals(email) && entry.getValue().equals(password)) {
                            loginSuccess = true;
                            Intent intent = new Intent(getApplicationContext(), SuccessActivity.class);
                            startActivity(intent);
                            break;
                        }
                    }
                    if (!loginSuccess)
                        Toast.makeText(getApplicationContext(), "Invalid Email or Password", Toast.LENGTH_SHORT).show();
                }
            });

        }


    }

    public void validateClient(){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RestClientApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RestClientApi api = retrofit.create(RestClientApi.class);

        Call<UserPOJO> userList = api.getAllUsers();



        userList.enqueue(new Callback<UserPOJO>() {
            @Override
            public void onResponse(Call<UserPOJO> call, Response<UserPOJO> response) {
                UserPOJO userPOJO = response.body();
                List<User> userList = userPOJO.getUsers();

                for(User user : userList){
                   hashMap.put(user.getEmail(),user.getPassword());

                }

            }

            @Override
            public void onFailure(Call<UserPOJO> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"Connection Failed.",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }



    }


