package com.praszapps.recaptchasample;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.safetynet.SafetyNet;
import com.google.android.gms.safetynet.SafetyNetApi;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.concurrent.Executor;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    public static final String SITE_KEY = "6LdvnmsUAAAAAOOqZ6iQtnl09wzbR6cdQyQnDl30";
    public static final String PVT_KEY = "6LdvnmsUAAAAAB79MwKgMMxu0";

    public RecaptchaAPI service;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SafetyNet.getClient(this).verifyWithRecaptcha(SITE_KEY)
                .addOnSuccessListener(this,
                        new OnSuccessListener<SafetyNetApi.RecaptchaTokenResponse>() {
                            @Override
                            public void onSuccess(SafetyNetApi.RecaptchaTokenResponse response) {
                                // Indicates communication with reCAPTCHA service was successful.
                                String userResponseToken=response.getTokenResult();
                                if (!userResponseToken.isEmpty()) {
                                    Retrofit retrofit = new Retrofit.Builder()
                                            .baseUrl("https://www.google.com")
                                            .addConverterFactory(GsonConverterFactory.create())
                                            .build();

                                    RecaptchaVerifyRequest recaptchaVerifyRequest = new RecaptchaVerifyRequest();
                                    recaptchaVerifyRequest.setResponse(userResponseToken);
                                    recaptchaVerifyRequest.setSecret(PVT_KEY);


                                    service = retrofit.create(RecaptchaAPI.class);
                                    service.verifyResponse(recaptchaVerifyRequest);

                                    Call<RecaptchaVerifyResponse> recaptchaVerifyResponseCall = service.verifyResponse(recaptchaVerifyRequest);
                                    recaptchaVerifyResponseCall.enqueue(new Callback<RecaptchaVerifyResponse>() {
                                        @Override
                                        public void onResponse(Call<RecaptchaVerifyResponse> call, Response<RecaptchaVerifyResponse> response) {
                                            if(response.body().isSuccess()) {
                                                Toast.makeText(MainActivity.this, "SUCCESS!!!", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(MainActivity.this, "FAILED!!!", Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<RecaptchaVerifyResponse> call, Throwable t) {
                                            Toast.makeText(MainActivity.this, "NOT DONE!!!!", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }
                        })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if (e instanceof ApiException) {
                            ApiException apiException = (ApiException) e;
                            int statusCode = apiException.getStatusCode();
                        } else {
                            // A unknown type of error occurred.
                            Log.d("", "Error: " + e.getMessage());
                        }
                    }
                });


    }
}
