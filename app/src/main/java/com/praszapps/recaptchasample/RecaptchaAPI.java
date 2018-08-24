package com.praszapps.recaptchasample;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

public interface RecaptchaAPI {

    @Headers("Content-Type: application/x-www-form-urlencoded; charset=utf-8")
    @POST("/recaptcha/api/siteverify")
    Call<RecaptchaVerifyResponse> verifyResponse(@QueryMap Map<String, String> params);
}