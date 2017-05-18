# Android App 多语言切换
最近公司的 App 里需要用到多语言切换，简单来说，就是如果用户没有选择语言选项时，App 默认跟随系统语言，如果用户在 App 内进行了语言设置，那么就使用用户设置的语言。当然，你会发现，App 的语言切换也会导致加载的其它资源文件进行切换

上述内容大概可以分为以下几块：

1. 获取系统默认的语言和地区(**注意地区，后面会讲述这里的坑**)
2. 更改 App 的语言

## Android 应用资源国际化
在正式开始之前，先来讲解一下 Android 应用资源国际化的知识。对于资源文件的国际化，我们一般是在 Android `src/main/res/` 目录下，建立对应语言文件夹，格式一般为：`values-语言代号-地区代号`，默认的资源是不包含**语言代号和地区代号**的。

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

## 更改 App 的语言设置

## 多语言切换中遇到的坑
1. 网络请求错误提示：Application 初始化时，网络请求错误提示语全部加载到内存，导致语言切换后未能更新
2. 资源文件夹`values-zh-rCN` 和 `values-zh` 导致的错误
3. App 多语言切换重启后失效
4. App 多语言切换设置持久化后，在应用启动时， Application 的 `onCreate()` 中进行切换，如果系统切换了语言，App 也会随之跟随系统语言

