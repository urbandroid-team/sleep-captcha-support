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
    compile 'com.urbandroid.sleep:captcha-support:0.1.2@aar'
}
```
## Captcha Manifest

Your captcha activity must be exported and single top. OPEN action is mandatory in intent filter otherwise
Sleep as Android will not find your captcha.

```xml
<manifest ... >

    <application ... >

        <activity
            android:name=".MyCleverCaptchaActivity"
            android:launchMode="singleTop"
            android:label="@string/captcha_label"
            android:exported="true"
            >
            <meta-data android:name="com.urbandroid.sleep.captcha.meta.has_difficulty" android:value="true"/>
            <intent-filter>
                <action android:name="com.urbandroid.sleep.captcha.intent.action.OPEN"/>
                <action android:name="com.urbandroid.sleep.captcha.intent.action.CONFIG"/>
            </intent-filter>
        </activity>
    </application>

</manifest>
```
## CaptchaSupport object

All interaction between Sleep and Captcha is covered by CaptchaSupport object. It must
be created inside onCreate method of Activity.

```java
    protected void onCreate(final Bundle savedInstanceState) {
        ...
        captchaSupport = CaptchaSupportFactory.create(this);
        ...
    }
```

When CaptchaSupport object is created you can
* check in which mode (Preview, Configuration, Operational) is catcha running by CaptchaSupport.getMode()
or CaptchaSupport.isPreviewMode(), isOperationalMode(), isConfigurationMode()
* get difficulty (1-5) - use CaptchaSupport.getDifficulty()
* set up time remaining listener
* call solved method when captcha is successfully resolved
* call unsolved method when captcha was not solved but user left activity
* call alive in order to reset timeout for solving captcha
* or use advanced features like CaptchaFinder for getting list of all available captchs on mobile
and launch them via CaptchaLauncher

## Difficulty
Put following lines in AndroidManifest file, in case your catcha need support several level of difficulty
```xml
    <activity ...>
        ...
        <meta-data android:name="com.urbandroid.sleep.captcha.meta.has_difficulty" android:value="true"/>
        ...
    </activity>
```
To get current captcha difficulty call CaptchaSuport.getDifficulty().
Use CaptchaDifficulty annotation to get all possible values.


## Recommendation
* CapchaSupport object must be created in activity onCreate method
* override activity onUserInteraction method to call CaptchaSupport.alive() method
* override activity onBackPressed method to call CaptchaSupport.unsolved()

## Read more and get started

Check simple github project captcha [examples] or read more on the [website][website].

[examples]: https://github.com/urbandroid-team/sleep-captcha-examples/
[issues]: https://github.com/urbandroid-team/sleep-captcha-support/issues
[website]: http://sleep.urbandroid.org/documentation/developer-api/captcha-api
