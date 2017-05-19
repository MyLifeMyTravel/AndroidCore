# Android App 多语言切换
最近公司的 App 里需要用到多语言切换，简单来说，就是如果用户没有选择语言选项时，App 默认跟随系统语言，如果用户在 App 内进行了语言设置，那么就使用用户设置的语言。当然，你会发现，App 的语言切换也会导致加载的其它资源文件进行切换

上述内容大概可以分为以下几块：

1. 获取系统默认的语言和地区(**注意地区，后面会讲述这里的坑**)
2. 更改 App 的语言

## Android 应用资源国际化
在正式开始之前，先来讲解一下 Android 应用资源国际化的知识。对于资源文件的国际化，我们一般是在 Android `src/main/res/` 目录下，建立对应语言文件夹，格式一般为：`values-语言代号-地区代号`，默认的资源是不包含**语言代号和地区代号**的。一般情况下，应用资源是没有做任何适配的，所以不管如何切换语言和地区设置，应用显示的资源都不会发生任何改变。

配置选项包括**语言代号**和**地区代号**。表示中文和中国的配置选项是 zh-rCN（zh表示中文，CN表示中国）。表示英文和美国的配置选项是 en-rUS（en表示英文，US表示美国）。同一语言代号可以有多个地区代号，用 r 表示区分。

常见的国际化资源表示形式：

```
中文（中国）：values-zh-rCN

中文（台湾）：values-zh-rTW

中文（香港）：values-zh-rHK

维吾尔文（中国）：values-ug-rCN

英语（美国）：values-en-rUS

英语（英国）：values-en-rGB

英文（澳大利亚）：values-en-rAU

英文（加拿大）：values-en-rCA

英文（爱尔兰）：values-en-rIE

英文（印度）：values-en-rIN

英文（新西兰）：values-en-rNZ

英文（新加坡）：values-en-rSG

英文（南非）：values-en-rZA

阿拉伯文（埃及）：values-ar-rEG

阿拉伯文（以色列）：values-ar-rIL

保加利亚文:  values-bg-rBG

加泰罗尼亚文：values-ca-rES

捷克文：values-cs-rCZ

丹麦文：values-da-rDK

德文（奥地利）：values-de-rAT

德文（瑞士）：values-de-rCH

德文（德国）：values-de-rDE

德文（列支敦士登）：values-de-rLI

希腊文：values-el-rGR

西班牙文（西班牙）：values-es-rES

西班牙文（美国）：values-es-rUS

芬兰文（芬兰）：values-fi-rFI

法文（比利时）：values-fr-rBE

法文（加拿大）：values-fr-rCA

法文（瑞士）：values-fr-rCH

法文（法国）：values-fr-rFR

希伯来文：values-iw-rIL

印地文：values-hi-rIN

克罗里亚文：values-hr-rHR

匈牙利文：values-hu-rHU

印度尼西亚文：values-in-rID

意大利文（瑞士）：values-it-rCH

意大利文（意大利）：values-it-rIT

日文：values-ja-rJP

韩文：values-ko-rKR

立陶宛文：valueslt-rLT

拉脱维亚文：values-lv-rLV

挪威博克马尔文：values-nb-rNO

荷兰文(比利时)：values-nl-BE

荷兰文（荷兰）：values-nl-rNL

波兰文：values-pl-rPL

葡萄牙文（巴西）：values-pt-rBR

葡萄牙文（葡萄牙）：values-pt-rPT

罗马尼亚文：values-ro-rRO

俄文：values-ru-rRU

斯洛伐克文：values-sk-rSK

斯洛文尼亚文：values-sl-rSI

塞尔维亚文：values-sr-rRS

瑞典文：values-sv-rSE

泰文：values-th-rTH

塔加洛语：values-tl-rPH

土耳其文：values–r-rTR

乌克兰文：values-uk-rUA

越南文：values-vi-rVN
```

## 获取系统默认的语言和地区
总的来说，获取语言和地区有三种方法：

1. 通过 Java 自带的接口来实现，即：

    ```java
    Locale locale = Locale.getLocale();
    String language = locale.getLanguage();
    String country = locale.getCountry();
    ```

2. 通过 `Configuration` 来获取

    ```java
    //方法1，该方法已废弃，如果在 API >= 17 的版本上使用 方法2
    Locale locale = context.getResources().getConfiguration().locale;
    //方法2，在 API >= 17 的版本上可以使用
    Locale locale = context.getResources().getConfiguration().getLocales().get(0);
    String language = locale.getLanguage();
    String country = locale.getCountry();
    ```
    其中， `context.getResources()` 也可以用 `Resources.getSystem()` 来代替，前者获取的是应用内部的语言和地区设置，后者获取的是系统的语言地区设置，默认情况下，前者跟随系统设置。

## 更改 App 的语言设置
通过上述分析，我们已经知道怎么获取系统和应用的语言地区设置了。接下来，我们来讲一下如何实现 Android App 的多语言切换。在前面我们已经使用到了 `Configuration` ，这个类保存了 Android 应用的所有设备信息，详见 [Configuration](https://developer.android.com/reference/android/content/res/Configuration.html)。要实现应用的多语言切换，我们所要做的就是更新 `Configuration` 中关于语言地区的属性。

```java
Resources resources = context.getResources();
DisplayMetrics metrics = resources.getDisplayMetrics();
Configuration configuration = resources.getConfiguration();
//API >= 17 可以使用
configuration.setLocale(locale);
//该方法已经废弃，官方建议使用 Context.createConfigurationContext(Configuration)
resources.updateConfiguration(configuration, metrics);
```

资源目录结构大致如下：

```
│   └── res
│       ├── drawable
│       ├── drawable-xhdpi
│       │   └── icon_test.png
│       ├── drawable-zh-rCN-xhdpi//图标适配
│       │   └── icon_test.png
│       ├── layout
│       │   └── activity_main.xml
│       ├── mipmap-hdpi
│       │   └── ic_launcher.png
│       ├── mipmap-mdpi
│       │   └── ic_launcher.png
│       ├── mipmap-xhdpi
│       │   └── ic_launcher.png
│       ├── mipmap-xxhdpi
│       │   └── ic_launcher.png
│       ├── mipmap-xxxhdpi
│       │   └── ic_launcher.png
│       ├── values
│       │   ├── colors.xml
│       │   ├── strings.xml
│       │   └── styles.xml
│       ├── values-en-rWW
│       │   └── strings.xml
│       ├── values-ja-rJP
│       │   └── strings.xml
│       ├── values-ko-rKR
│       │   └── strings.xml
│       └── values-zh-rCN
│           └── strings.xml
```

### 重新加载资源
看到这里，你是不是觉得就结束了？不，当然不是，更新 `Configuration` 之后，如果不重启 Activity，应用的资源就不会被重新加载。

```java
Intent intent = new Intent(this, MainActivity.class);
//开始新的activity同时移除之前所有的activity
intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
startActivity(intent);
```

### 持久化存储设置
经过上述步骤，我们已经可以看到应用显示的资源发生了改变，但是当应用被杀掉重启后，之前所有的设置都已经失效，应用的语言地区又变成了系统默认的，这是因为我们对应用所做的变更只是保存在内存中，当应用被杀掉，在内存中的数据也随之被释放，再次启动应用的时候，应用读取的是系统的 `Configuration` ，语言地区也随之变成系统默认的。

当应用需要保存用户更改的操作，就需要对用户的选择进行持久化，并在应用重启的时候，从配置中读取并应用该配置。

```java
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        //一般在 Application 的 onCreate() 方法中更新 Configuration
        LanguageUtil.changeAppLanguage(this, Locale.SIMPLIFIED_CHINESE, true);
    }
}
```

`LanguageUtil.java`

```java
/**
* 更改应用语言
*
* @param context
* @param locale 语言地区
* @param persistence 是否持久化
*/
public static void changeAppLanguage(Context context, Locale locale,
                                    boolean persistence) {
   Resources resources = context.getResources();
   DisplayMetrics metrics = resources.getDisplayMetrics();
   Configuration configuration = resources.getConfiguration();
   if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
       configuration.setLocale(locale);
   } else {
       configuration.locale = locale;
   }
   resources.updateConfiguration(configuration, metrics);
   if (persistence) {
       saveLanguageSetting(context, locale);
   }
}

private static void saveLanguageSetting(Context context, Locale locale) {
   String name = context.getPackageName() + "_" + LANGUAGE;
   SharedPreferences preferences =
           context.getSharedPreferences(name, Context.MODE_PRIVATE);
   preferences.edit().putString(LANGUAGE, locale.getLanguage()).apply();
   preferences.edit().putString(COUNTRY, locale.getCountry()).apply();
}
```

这样，Android 应用内多语言切换基本完工。接下来，分享一下我在多语言切换过程中遇到的坑。

## 多语言切换中遇到的坑

1. 以静态变量的方式，在 Application 初始化时初始化网络请求错误提示语，然后再系统中切换语言后，网络请求错误提示语未更新。

    解决办法：使用时直接通过 `getString()` 方法获取

2. App 多语言切换设置持久化后，在应用启动时， Application 的 `onCreate()` 中也进行了多语言切换。然后去系统设置中切换语言，App 也会随之跟随系统语言。

    原因：在我们改变系统的语言时，应用的 `Configuration` 也随之跟随系统改变，而不是我们启动应用时的设置了

    解决办法：监听 Activity 的生命周期，在 Activty 的 `onCreate()` 中判断应用当前的语言设置是否与用户设置值相同，否则强制更新应用语言设置。因为，当系统切换语言选项的时候，系统会重启 Activity，就如前文所说，我们需要重启 Activity 才能实现资源的重新加载一样。这里也有两种方案：
    1. 创建一个基类 `BaseActivity` ，在其 `onCreate()` 方法中做处理
    2. 使用 `ActivityLifecycleCallbacks` ，在其回调 `onActivityCreated()` 中做处理

    对比一下，上述两种方案，第一种只能针对继承自 `BaseActivity` 的才有效，第二种则是监听所以 Activity 的生命周期。所以相对而言，第二种方案更好点。

    ```java
    /**
    * 判断是否与设定的语言相同.
    *
    * @param context
    * @return
    */
    public static boolean isSameWithSetting(Context context) {
       Locale current = context.getResources().getConfiguration().locale;
       return current.equals(getAppLocale(context));
    }
    ```

    ```java
    public class App extends Application {

        @Override
        public void onCreate() {
            super.onCreate();
            LanguageUtil.init(this);
            //注册Activity生命周期监听回调
            registerActivityLifecycleCallbacks(callbacks);
        }

        ActivityLifecycleCallbacks callbacks = new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                //强制修改应用语言
                if (!LanguageUtil.isSameWithSetting(activity)) {
                    LanguageUtil.changeAppLanguage(activity,
                            LanguageUtil.getAppLocale(activity));
                }
            }
            //Activity 其它生命周期的回调
        };
    }
    ```

3. 对于在 AndroidManifest.xml 中配置 `launchMode` 为 `singleInstance` 的 Activity，使用

    ```java
    Intent intent = new Intent(this, MainActivity.class);
    //开始新的activity同时移除之前所有的activity
    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
    startActivity(intent);
    ```
    资源文件不更新。

    原因：`launchMode` 为 `singleInstance` 的 Activity 与当前应用时不在同一个 Task 栈

    解决方法：将 `launchMode` 改为其它模式或者杀掉应用重新启动

4. 资源文件夹为 `values-zh-rCN`时，将应用 Locale 设置为 `Locale.CHINESE` 时，找不到对应的资源文件。

    原因：`values-zh-rCN` 对应的 Locale 为 `Locale.SIMPLIFIED_CHINESE`

    解决办法：将 Locale 设置为 `Locale.SIMPLIFIED_CHINESE` 或者将资源文件改为 `values-zh`
    这是踩得最惨的一个坑，浪费了大量时间，所以才会有开头 **Android 应用资源国际化** 那么一小节插曲。



