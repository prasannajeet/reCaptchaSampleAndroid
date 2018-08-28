package com.praszapps.recaptchasample.view;

import android.app.AlertDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.google.android.gms.safetynet.SafetyNet;
import com.google.android.gms.safetynet.SafetyNetApi;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.praszapps.recaptchasample.R;
import com.praszapps.recaptchasample.model.RecaptchaVerifyResponse;
import com.praszapps.recaptchasample.viewmodel.RecaptchaResponseViewModel;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SafetyNet.getClient(MainActivity.this).verifyWithRecaptcha(getResources().getString(R.string.pubK))
                        .addOnSuccessListener(new SuccessListener())
                        .addOnFailureListener(new FailureListener());
            }
        });
    }

    private void showAlertWithButton(@NonNull String title, @NonNull String message, @NonNull String buttonMessage) {
        new AlertDialog.Builder(this).setTitle(title).setMessage(message).setCancelable(false).setPositiveButton(buttonMessage, null).create().show();
    }

    private class SuccessListener implements OnSuccessListener<SafetyNetApi.RecaptchaTokenResponse> {

        @Override
        public void onSuccess(final SafetyNetApi.RecaptchaTokenResponse recaptchaTokenResponse) {
            {
                RecaptchaResponseViewModel mViewModel = ViewModelProviders.of(MainActivity.this).get(RecaptchaResponseViewModel.class);
                String userResponseToken = recaptchaTokenResponse.getTokenResult();
                if (!userResponseToken.isEmpty()) {
                    mViewModel.getmRecaptchaObservable("https://www.google.com", userResponseToken, getApplicationContext().getString(R.string.priK)).observe(MainActivity.this, new Observer<RecaptchaVerifyResponse>() {
                        @Override
                        public void onChanged(@Nullable RecaptchaVerifyResponse recaptchaVerifyResponse) {
                            if (recaptchaVerifyResponse != null && recaptchaVerifyResponse.isSuccess()) {
                                showAlertWithButton("Obie is a human", "Yes Siree, he a human I tell ya", "Well now ain't that nice!");
                            } else {
                                showAlertWithButton("Obie ain't a human", "No Siree, Obie ain't no human at all", "Doggone it!");
                            }
                        }
                    });
                }
            }
        }
    }

    private class FailureListener implements OnFailureListener {

        @Override
        public void onFailure(@NonNull Exception e) {
            showAlertWithButton("Obie is Unknown", e.getLocalizedMessage(), "Doggone it!");
        }
    }
}