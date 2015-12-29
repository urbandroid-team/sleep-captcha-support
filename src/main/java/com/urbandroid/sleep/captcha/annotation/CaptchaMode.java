package com.urbandroid.sleep.captcha.annotation;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Captcha mode enum.
 *
 * <p><strong>Possible values:</strong>
 * <ul>
 * <li>CaptchaMode.CAPTCHA_MODE_OPERATIONAL</li>
 * <li>CaptchaMode.CAPTCHA_MODE_PREVIEW</li>
 * <li>CaptchaMode.CAPTCHA_MODE_CONFIGURATION</li>
 * </ul>
 */
@IntDef({
        CaptchaMode.CAPTCHA_MODE_OPERATIONAL,
        CaptchaMode.CAPTCHA_MODE_PREVIEW,
        CaptchaMode.CAPTCHA_MODE_CONFIGURATION
})
@Retention(RetentionPolicy.SOURCE)
public @interface CaptchaMode {

    int CAPTCHA_MODE_OPERATIONAL = 1;
    int CAPTCHA_MODE_PREVIEW = 2;
    int CAPTCHA_MODE_CONFIGURATION = 3;
}
