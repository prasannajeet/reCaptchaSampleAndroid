package com.praszapps.recaptchasample.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RecaptchaVerifyResponse {

    private boolean success;
    private String challenge_ts;
    private String apk_package_name;
    @SerializedName("error-codes")
    private List<String> errorCodes;

    public boolean isSuccess() {
        return success;
    }

    public String getChallenge_ts() {
        return challenge_ts;
    }

    public String getApk_package_name() {
        return apk_package_name;
    }

    public List<String> getErrorCodes() {
        return errorCodes;
    }
}