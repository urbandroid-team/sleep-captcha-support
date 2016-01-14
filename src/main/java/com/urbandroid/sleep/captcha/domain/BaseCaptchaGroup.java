package com.urbandroid.sleep.captcha.domain;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BaseCaptchaGroup implements CaptchaGroup {

    private final String id;
    private final String label;
    private final List<CaptchaInfo> captchaInfos = new ArrayList<>();

    public BaseCaptchaGroup(final @NonNull String id, final @NonNull String label) {
        this(id, label, Collections.<CaptchaInfo>emptyList());
    }

    public BaseCaptchaGroup(final @NonNull String id, final @NonNull String label, final @NonNull List<CaptchaInfo> captchaInfos) {
        this.id = id;
        this.label = label;
        this.captchaInfos.addAll(captchaInfos);
    }

    @Override
    @NonNull
    public String getId() {
        return id;
    }

    @Override
    @NonNull
    public String getLabel() {
        return label;
    }

    @Override
    public BaseCaptchaGroup add(final @NonNull CaptchaInfo captchaInfo) {
        captchaInfos.add(captchaInfo);
        return this;
    }

    @Override
    @NonNull
    public List<CaptchaInfo> getCaptchaInfos() {
        return captchaInfos;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BaseCaptchaGroup that = (BaseCaptchaGroup) o;

        return id.equals(that.id);

    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
