# Sleep as Android - Captcha support

## Introduction

This open-source library contains classes that are useful for developing
additional captcha into Sleep as Android application.

The library is under heavy development :-), but ready for use. Check the
[issue tracker][issues] to see what's happening.


## Usage

When you use Gradle add the following maven repository and dependency with the latest version
to your `build.gradle` file to use the library:

```groovy
repositories {
    jcenter()
}

dependencies {
    compile 'com.urbandroid.sleep:captcha-support:0.0.9@aar'
}
```
## Captcha Manifest

Your captcha activity must be exported and single top. OPEN action is mandatory in intent filter otherwise
Sleep as Android will not find your captcha.

```xml
<manifest

    <application
        android:launchMode="singleTop"
    >

        <activity
            android:name=".MyCleverCaptchaActivity"
            android:label="@string/captcha_label"
            android:exported="true"
            >
            <meta-data android:name="com.urbandroid.sleep.captcha.meta.has_difficulty" android:value="true"/>
            <intent-filter>
                <action android:name="android.intent.action.MAIN"/>
                <action android:name="com.urbandroid.sleep.captcha.intent.action.OPEN"/>
                <action android:name="com.urbandroid.sleep.captcha.intent.action.CONFIG"/>
                <category android:name="android.intent.category.LAUNCHER"/>
            </intent-filter>
        </activity>
    </application>

</manifest>
```

## Captcha Mode

* Preview
* Configuration
* Operational

## Captcha Difficulty

* Very Simple
* Simple
* CaptchaDifficulty.INTERMEDIATE
* CaptchaDifficulty.HARD
* CaptchaDifficulty.VERY_HARD


## Read more and get started

Read more on the [website][website].

[issues]: https://github.com/urbandroid-team/sleep-captcha-support/issues
[website]: http://sleep.urbandroid.org/documentation/developer-api/captcha-api
