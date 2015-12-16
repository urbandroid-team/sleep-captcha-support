package com.urbandroid.sleep.captcha.annotation;

import android.support.annotation.IntDef;

import com.urbandroid.sleep.captcha.CaptchaConstant;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IntDef({
        CaptchaConstant.VERY_SIMPLE,
        CaptchaConstant.SIMPLE,
        CaptchaConstant.INTERMEDIATE,
        CaptchaConstant.HARD,
        CaptchaConstant.VERY_HARD
})
@Retention(RetentionPolicy.SOURCE)
public @interface CaptchaDifficulty {
}
