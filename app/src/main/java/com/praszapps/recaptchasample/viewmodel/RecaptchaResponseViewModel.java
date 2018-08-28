package com.praszapps.recaptchasample.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.praszapps.recaptchasample.model.RecaptchaRepository;
import com.praszapps.recaptchasample.model.RecaptchaVerifyResponse;

public class RecaptchaResponseViewModel extends AndroidViewModel {

    public RecaptchaResponseViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<RecaptchaVerifyResponse> getmRecaptchaObservable(@NonNull String baseUrl, @NonNull String response, @NonNull String key) {
        return new RecaptchaRepository().doRecaptchaValidation(baseUrl, response, key);
    }
}