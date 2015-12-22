package com.urbandroid.sleep.captcha;

public interface CaptchaConstant {

    String TAG = "captcha-support";
    String SLEEP_PACKAGE = "com.urbandroid.sleep";

    String CAPTCHA_BACK_INFO  = "captchaInfo";

    String CAPTCHA_ACTION_LAUNCH = "com.urbandroid.sleep.captcha.intent.action.OPEN";
    String CAPTCHA_ACTION_CONFIG = "com.urbandroid.sleep.captcha.intent.action.CONFIG";
    String CAPTCHA_ACTION_SOLVED = "com.urbandroid.sleep.captcha.intent.action.SOLVED";

    String CAPTCHA_ACTION_ALIVE = "com.urbandroid.sleep.ACTION_DELAY_RESUMER";

    String SUCCESS = "success";
    String PREVIEW = "preview";
    String CAPTCHA_CONFIG_SUSPEND_VOLUME_MODE = "suspend-volume-mode";
    String CAPTCHA_CONFIG_DIFFICULTY = "difficulty";

    String ALARM_ACTIVITY_CLASS = "alarm_class";
    String ACTION_FINISH_CAPTCHA = "com.urbandroid.sleep.ACTION_FINISH_CAPTCHA";

    String META_BACK_COMPATIBILITY_ID   = "com.urbandroid.sleep.captcha.meta.id";
    String META_ORDER                   = "com.urbandroid.sleep.captcha.meta.order";
    String META_HAS_DIFFICULTY          = "com.urbandroid.sleep.captcha.meta.has_difficulty";

}
