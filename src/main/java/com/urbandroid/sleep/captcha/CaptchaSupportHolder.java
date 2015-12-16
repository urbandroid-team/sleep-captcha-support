package com.urbandroid.sleep.captcha;

/**
 * Helper class for cases when captcha support object need to be used
 * within several activities
 */
public class CaptchaSupportHolder {

    private static CaptchaSupport captchaSupport;

    public static CaptchaSupport get() {
        return captchaSupport;
    }

    static void set(final CaptchaSupport captchaSupport) {
        CaptchaSupportHolder.captchaSupport = captchaSupport;
    }
}
