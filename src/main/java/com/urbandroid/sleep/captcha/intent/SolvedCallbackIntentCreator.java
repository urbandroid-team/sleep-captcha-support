package com.urbandroid.sleep.captcha.intent;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

public interface SolvedCallbackIntentCreator {

    Intent createSolvedIntent(@NonNull Context context);
    Intent createUnsolvedIntent(@NonNull Context context);

}
