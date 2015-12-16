package com.urbandroid.sleep.captcha.annotation;

import android.support.annotation.IntDef;

import com.urbandroid.sleep.captcha.CaptchaConstant;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IntDef({
        CaptchaConstant.CAPTCHA_MODE_OPERATIONAL,
        CaptchaConstant.CAPTCHA_MODE_PREVIEW,
        CaptchaConstant.CAPTCHA_MODE_CONFIGURATION,
})
@Retention(RetentionPolicy.SOURCE)
public @interface CaptchaMode {
}
