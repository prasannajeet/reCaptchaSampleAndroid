package com.praszapps.recaptchasample;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.safetynet.SafetyNet;
import com.google.android.gms.safetynet.SafetyNetApi;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.HashMap;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private static final String SITE_KEY = "6LdvnmsUAAAAAOOqZ6iQtnl09wzbR6cdQyQnDl30";
    private static final String PVT_KEY = "6LdvnmsUAAAAAB79MwKgMMxu0-FhZj_T6WpLoLd1";

    private SuccessListener mSuccessListener = new SuccessListener();
    private FailureListener mFailureListener = new FailureListener();
    private ApiResponseCall mApiResponseCall = new ApiResponseCall();

    private Button reCaptchaButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        reCaptchaButton = findViewById(R.id.button);
        reCaptchaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SafetyNet.getClient(MainActivity.this).verifyWithRecaptcha(SITE_KEY)
                        .addOnSuccessListener(mSuccessListener)
                        .addOnFailureListener(mFailureListener);
            }
        });
    }

    private RecaptchaAPI getService() {
        return getRetrofit().create(RecaptchaAPI.class);
    }

    private Retrofit getRetrofit() {
        return new Retrofit.Builder()
                .baseUrl("https://www.google.com")
                .client(getOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    private OkHttpClient getOkHttpClient() {
        return new OkHttpClient.Builder().addInterceptor(getLoggingInterceptor()).build();
    }

    private HttpLoggingInterceptor getLoggingInterceptor() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return interceptor;
    }

    private void retrofitServiceCall(String userResponseToken) {
        RecaptchaAPI service = getService();

        Map<String, String> params = new HashMap<>();
        params.put("response", userResponseToken);
        params.put("secret", PVT_KEY);

        Call<RecaptchaVerifyResponse> recaptchaVerifyResponseCall = service.verifyResponse(params);
        recaptchaVerifyResponseCall.enqueue(mApiResponseCall);
    }

    private class SuccessListener implements OnSuccessListener<SafetyNetApi.RecaptchaTokenResponse> {

        @Override
        public void onSuccess(SafetyNetApi.RecaptchaTokenResponse recaptchaTokenResponse) {
            {
                // Indicates communication with reCAPTCHA service was successful.
                String userResponseToken = recaptchaTokenResponse.getTokenResult();
                if (!userResponseToken.isEmpty()) {
                    retrofitServiceCall(userResponseToken);
                }
            }
        }
    }

    private void showDialog(String title, String message) {
        new AlertDialog.Builder(this).setTitle(title).setMessage(message).setCancelable(false).setPositiveButton("Well now ain't that nice!", null).create().show();
    }

    private class FailureListener implements OnFailureListener {

        @Override
        public void onFailure(@NonNull Exception e) {
            showDialog("Obie is Unknown", e.getLocalizedMessage());
        }
    }

    private class ApiResponseCall implements Callback<RecaptchaVerifyResponse> {
        @Override
        public void onResponse(Call<RecaptchaVerifyResponse> call, Response<RecaptchaVerifyResponse> response) {
            if (response.body().isSuccess()) {
                showDialog("Obie is a human", "Yes Siree, he a human I tell ya");
            } else {

                showDialog("Obie ain't a human", "No Siree, Obie ain't no human at all");
            }
        }

        @Override
        public void onFailure(Call<RecaptchaVerifyResponse> call, Throwable t) {
            showDialog(t.getMessage(), t.getStackTrace().toString());
        }
    }
}
