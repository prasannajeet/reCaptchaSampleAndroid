package com.praszapps.recaptchasample.model;

public class RecaptchaVerifyResponse {

    private boolean success;
    private String challenge_ts;
    private String apk_package_name;

    public boolean isSuccess() {
        return success;
    }

    public String getChallenge_ts() {
        return challenge_ts;
    }
    public String getApk_package_name() {
        return apk_package_name;
    }

}