package com.urbandroid.sleep.captcha.annotation;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 *  Captcha event enum.
 *
 * <p><strong>Possible values:</strong>
 * <ul>
 * <li>CaptchaEvent.CAPTCHA_BACK_INTENT_SOLVED</li>
 * <li>CaptchaEvent.CAPTCHA_BACK_INTENT_UNSOLVED</li>
 * <li>CaptchaEvent.CAPTCHA_BACK_INTENT_ALIVE</li>
 * </ul>
 */
@StringDef({
        CaptchaEvent.CAPTCHA_BACK_INTENT_SOLVED,
        CaptchaEvent.CAPTCHA_BACK_INTENT_UNSOLVED,
        CaptchaEvent.CAPTCHA_BACK_INTENT_ALIVE
})
@Retention(RetentionPolicy.SOURCE)
public @interface CaptchaEvent {

        @CaptchaEvent String CAPTCHA_BACK_INTENT_SOLVED = "solved";
        @CaptchaEvent String CAPTCHA_BACK_INTENT_UNSOLVED = "unsolved";
        @CaptchaEvent String CAPTCHA_BACK_INTENT_ALIVE  = "alive";

}
