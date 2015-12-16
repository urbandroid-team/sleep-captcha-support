package com.urbandroid.sleep.captcha.annotation;

import android.support.annotation.StringDef;

import com.urbandroid.sleep.captcha.CaptchaConstant;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@StringDef({
        CaptchaConstant.OPERATION_NONE,
        CaptchaConstant.SNOOZE_CANCELED,
        CaptchaConstant.SNOOZE_CANCELED,
        CaptchaConstant.SHOULD_SKIP,
        CaptchaConstant.DELETE_ALARM,
        CaptchaConstant.DISABLE_ALARM,
        CaptchaConstant.EDIT_ALARM,
        CaptchaConstant.EDIT_ALARM_TIME_EXTRA,
        CaptchaConstant.CAPTCHA_START_FOR_RESULT
})
@Retention(RetentionPolicy.SOURCE)
public @interface SleepOperation {
}
