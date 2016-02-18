package com.urbandroid.sleep.captcha;

public interface CaptchaConstant {

    String TAG = "captcha-support";
    String SLEEP_PACKAGE = "com.urbandroid.sleep";

    String CAPTCHA_BACK_INFO  = "captchaInfo";

    /* parent captcha id from which current captcha was launched */
    String CAPTCHA_PARENT_ID = "captchaParentId";
    String CAPTCHA_PARENT_MODE = "captchaParentMode";
    String CAPTCHA_ID  = "captchaId";
    String CAPTCHA_ORIGIN_INTENT  = "originIntent";

    String CAPTCHA_ACTION_LAUNCH = "com.urbandroid.sleep.captcha.intent.action.OPEN";
    String CAPTCHA_ACTION_CONFIG = "com.urbandroid.sleep.captcha.intent.action.CONFIG";
    String CAPTCHA_ACTION_SOLVED = "com.urbandroid.sleep.captcha.intent.action.SOLVED";
    String CAPTCHA_ACTION_ALIVE = "com.urbandroid.sleep.captcha.intent.action.ALIVE";

    String SUCCESS = "success";
    String PREVIEW = "preview";
    String TIME_ADD = "timeAddInSeconds";
    String CAPTCHA_CONFIG_ALIVE_TIMEOUT = "aliveTimeout";
    String CAPTCHA_CONFIG_SUPPRESS_ALARM_MODE = "suppress-alarm-mode";
    String CAPTCHA_CONFIG_DIFFICULTY = "difficulty";

    String ACTION_FINISH_CAPTCHA = "com.urbandroid.sleep.ACTION_FINISH_CAPTCHA";

    String META_BACK_COMPATIBILITY_ID   = "com.urbandroid.sleep.captcha.meta.id";
    String META_ORDER                   = "com.urbandroid.sleep.captcha.meta.order";
    String META_FOR_CAPTCHA             = "com.urbandroid.sleep.captcha.meta.for_captcha";
    String META_HAS_DIFFICULTY          = "com.urbandroid.sleep.captcha.meta.has_difficulty";

    String ALARM_SNOOZE_ACTION = "com.urbandroid.sleep.alarmclock.ALARM_SNOOZE";

}
