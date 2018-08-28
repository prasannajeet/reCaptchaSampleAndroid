package com.praszapps.recaptchasample.model;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import java.util.HashMap;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RecaptchaRepository {

    private RecaptchaVerificationService mService;

    public RecaptchaRepository() {
        this.mService = getService();
    }

    public LiveData<RecaptchaVerifyResponse> doRecaptchaValidation(String response, String key) {
        final MutableLiveData<RecaptchaVerifyResponse> data = new MutableLiveData<>();
        Map<String, String> params = new HashMap<>();
        params.put("response", response);
        params.put("secret", key);
        mService.verifyResponse(params).enqueue(new Callback<RecaptchaVerifyResponse>() {
            @Override
            public void onResponse(Call<RecaptchaVerifyResponse> call, Response<RecaptchaVerifyResponse> response) {
                data.setValue(response.body());
            }

            @Override
            public void onFailure(Call<RecaptchaVerifyResponse> call, Throwable t) {
                data.setValue(null);
            }
        });
        return data;
    }

    private RecaptchaVerificationService getService() {
        return getRetrofit().create(RecaptchaVerificationService.class);
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
}
