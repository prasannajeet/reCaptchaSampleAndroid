package com.praszapps.recaptchasample;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RecaptchaAPI {

    @POST("/recaptcha/api/siteverify")
    Call<RecaptchaVerifyResponse> verifyResponse(@Body RecaptchaVerifyRequest request);
}