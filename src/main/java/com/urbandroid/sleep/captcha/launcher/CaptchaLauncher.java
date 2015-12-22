package com.urbandroid.sleep.captcha.launcher;

import android.content.Intent;
import android.support.annotation.NonNull;

import com.urbandroid.sleep.captcha.annotation.CaptchaDifficulty;
import com.urbandroid.sleep.captcha.annotation.CaptchaMode;
import com.urbandroid.sleep.captcha.annotation.SleepOperation;
import com.urbandroid.sleep.captcha.domain.CaptchaInfo;
import com.urbandroid.sleep.captcha.intent.CallbackIntentCreator;

public interface CaptchaLauncher {

    CaptchaLauncher difficulty(@CaptchaDifficulty int difficulty);
    CaptchaLauncher operation(@SleepOperation String operation);
    CaptchaLauncher callbackIntentCreator(@NonNull CallbackIntentCreator callbackIntentCreator);
    CaptchaLauncher addFlags(int flags);
    CaptchaLauncher mode(@CaptchaMode int mode);
    void start(@NonNull CaptchaInfo captchaInfo);

    @NonNull
    Intent prepareIntent(@NonNull CaptchaInfo captchaInfo);


}
