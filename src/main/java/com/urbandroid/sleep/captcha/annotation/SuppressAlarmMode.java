package com.urbandroid.sleep.captcha.annotation;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IntDef({
        CaptchaMode.CAPTCHA_MODE_OPERATIONAL,
        CaptchaMode.CAPTCHA_MODE_PREVIEW,
        CaptchaMode.CAPTCHA_MODE_CONFIGURATION
})
@Retention(RetentionPolicy.SOURCE)
public @interface SuppressAlarmMode {

    @SuppressAlarmMode int FULL_ALARM_VOLUME = 0;
    @SuppressAlarmMode int LOW_ALARM_VOLUME = 1;
    @SuppressAlarmMode int ALARM_IS_SILENT = 2;

}
