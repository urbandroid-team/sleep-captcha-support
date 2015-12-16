package com.urbandroid.sleep.captcha.annotation;

import android.support.annotation.StringDef;

import com.urbandroid.sleep.captcha.CaptchaConstant;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by marcel on 11.12.15.
 */
@StringDef({
        CaptchaConstant.CAPTCHA_BACK_INTENT_SOLVED,
        CaptchaConstant.CAPTCHA_BACK_INTENT_ALIVE
})
@Retention(RetentionPolicy.SOURCE)
public @interface CaptchaEvent {
}
