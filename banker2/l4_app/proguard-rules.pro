# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/Beanu/Library/Android/sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
# Keep the support library
-keep class android.support.v4.** { *; }
-keep interface android.support.v4.** { *; }
# Keep the support library
-keep class android.support.v7.** { *; }
-keep interface android.support.v7.** { *; }

-keep class android.support.v7.widget.RoundRectDrawable { *; }
-dontwarn android.support.design.**
-keep class android.support.design.** { *; }
-keep interface android.support.design.** { *; }
-keep public class android.support.design.R$* { *; }

#本身
-keep class com.beanu.l4_bottom_tab.model.**{ *; }
-keep class com.beanu.arad.support.recyclerview.loadmore.LoadMorePresenterImpl { *; }
-keep public class com.tencent.bugly.**{*;}

#TalkingData
-dontwarn com.tendcloud.tenddata.**
-keep class com.tendcloud.** {*;}
-keep public class com.tendcloud.tenddata.** { public protected *;}
-keepclassmembers class com.tendcloud.tenddata.**{
public void *(***);
}
-keep class com.talkingdata.sdk.TalkingDataSDK {public *;}
-keep class com.apptalkingdata.** {*;}

#极光混淆
-dontoptimize
-dontpreverify

-dontwarn cn.jpush.**
-keep class cn.jpush.** { *; }

-dontwarn cn.jiguang.**
-keep class cn.jiguang.** { *; }


#Retrofit
-dontnote retrofit2.Platform
-dontnote retrofit2.Platform$IOS$MainThreadExecutor
-dontwarn retrofit2.Platform$Java8
-keepattributes Signature
-keepattributes Exceptions
-dontwarn retrofit2.adapter.rxjava.CompletableHelper$**

#Rxjava
-dontwarn sun.misc.**

-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
   long producerIndex;
   long consumerIndex;
}

-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode producerNode;
}

-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode consumerNode;
}


#OKHTTP

-dontwarn com.squareup.okhttp.
-keep class com.squareup.okhttp.{ *; }
-keep class com.zhy.http.okhttp.{ *; }
-keep interface com.squareup.okhttp. { *; }
-dontwarn okio.*
-keep class com.google.gson. { *; }
-keep class com.google.gson.JsonObject { *; }

#Eventbus
-keepattributes *Annotation*
-keepclassmembers class ** {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }

# Only required if you use AsyncExecutor
-keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
    <init>(java.lang.Throwable);
}


#Glide
-keep class com.bumptech.glide.integration.okhttp.OkHttpGlideModule


# ButterKnife 混淆代码
-keep class butterknife.** { *; }
-dontwarn butterknife.internal.**
-keep class **$$ViewBinder { *; }
-keepclasseswithmembernames class * {
    @butterknife.* <fields>;
}
-keepclasseswithmembernames class * {
    @butterknife.* <methods>;
}


#pay
-dontwarn com.tsy.sdk.pay.**
-keep class com.tsy.sdk.pay.**{*;}

#weixin
-dontwarn  com.tencent.**
-keep class com.tencent.** {*;}

#alipay
-dontwarn com.alipay.**
-keep class com.alipay.** {*;}

-dontwarn  com.ta.utdid2.**
-keep class com.ta.utdid2.** {*;}

-dontwarn  com.ut.device.**
-keep class com.ut.device.** {*;}

#ipay
-dontwarn  com.beanu.l2_pay.**
-keep public class com.beanu.l2_pay.** {
    !private <fields>;
    !private <methods>;
}

-keep public interface com.beanu.l2_pay.**{*;}
-keep public enum com.beanu.l2_pay.**{*;}

#Arad
-keep class com.beanu.arad.** { *; }
-keep interface com.beanu.arad.** { *; }
-keep class com.litesuits.orm.** { *; }
-keep interface com.litesuits.orm.** { *; }