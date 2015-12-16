package com.urbandroid.sleep.captcha;

public interface CaptchaConstant {

    String TAG = "captcha-support";

    String CAPTCHA_BACK_INTENT_SOLVED = "solved";
    String CAPTCHA_BACK_INTENT_ALIVE  = "alive";
    String CAPTCHA_BACK_INFO  = "captchaInfo";

    int CAPTCHA_MODE_OPERATIONAL = 1;
    int CAPTCHA_MODE_PREVIEW = 2;
    int CAPTCHA_MODE_CONFIGURATION = 3;

    String CAPTCHA_ACTION_LAUNCH = "com.urbandroid.sleep.captcha.intent.action.OPEN";
    String CAPTCHA_ACTION_CONFIG = "com.urbandroid.sleep.captcha.intent.action.CONFIG";
    String CAPTCHA_ACTION_SOLVED = "com.urbandroid.sleep.captcha.intent.action.SOLVED";
    String CAPTCHA_ACTION_ALIVE = "com.urbandroid.sleep.captcha.intent.action.ALIVE";

    String SUCCESS = "success";
    String PREVIEW = "preview";
    String CAPTCHA_CONFIG_SUSPEND_VOLUME_MODE = "suspend-volume-mode";
    String CAPTCHA_CONFIG_DIFFICULTY = "difficulty";

    int VERY_SIMPLE = 1;
    int SIMPLE = 2;
    int INTERMEDIATE = 3;
    int HARD = 4;
    int VERY_HARD = 5;


    String OPERATION_NONE = "no_operation";
    String SNOOZE_CANCELED = "snooze_cancel";
    String SHOULD_SKIP = "should_skip";
    String DELETE_ALARM = "delete_alarm";
    String DISABLE_ALARM = "disable_alarm";
    String EDIT_ALARM = "edit_alarm";
    String EDIT_ALARM_TIME_EXTRA = "edit_alarm_time_extra";
    String CAPTCHA_START_FOR_RESULT = "captcha_start_for_result";

    String ALARM_ACTIVITY_CLASS = "alarm_class";
    String ACTION_FINISH_CAPTCHA = "com.urbandroid.sleep.ACTION_FINISH_CAPTCHA";

    String META_BACK_COMPATIBILITY_ID   = "com.urbandroid.sleep.captcha.meta.id";
    String META_ORDER                   = "com.urbandroid.sleep.captcha.meta.order";
    String META_HAS_DIFFICULTY          = "com.urbandroid.sleep.captcha.meta.has_difficulty";

}
