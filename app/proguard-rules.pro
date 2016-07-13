# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\Android SDK/tools/proguard/proguard-android.txt
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
#
# -dontwarn android.support.v4.** //对于v4中找不到相应的类和方法,在编译时不警告
# -keep class android.support.v4.** { *; } //对于v4中的类不进行代码混淆
# -keep interface android.support.v4.app.** { *; } //对于v4中的接口不进行代码混淆
#}
-verbose
-keepattributes Signature

-keep class com.tencent.mobileqq.** {*;}
-dontnote com.tencent.mobileqq.**

-keep class com.android.** {*;}
-keep class android.** {*;}
-keep class android.**$** {*;}

-dontnote android.**
-dontnote android.**$**
-dontnote com.android.**






-keepclassmembers class * implements java.io.Serializable { *; }


#混淆时仍被意外混淆或抛弃的类

-keep class cn.sharesdk.**{*;}
-keep class com.sina.**{*;}
-keep class **.R$* {*;}
-keep class **.R{*;}

-keep class com.mob.**{*;}
-dontwarn com.mob.**
-dontwarn cn.sharesdk.**
-dontwarn **.R$*

#javaBean不被混淆
-keep class com.example.tjwx_person.bean.** { *; }
-keep class com.example.tjwx_person.http.**

#----------------------第三方库的配置--------------------------------
-dontwarn eclipse.local.sdk.**
-dontwarn  org.eclipse.jdt.annotation.**

-libraryjars  <java.home>/lib/rt.jar

#-libraryjars libs/android-support-v4.jar
-keep class android.support.**
-dontwarn android.support.**
-dontnote android.support.**


#腾讯开放平台
-keep class com.tencent.a.**
-keep class com.tencent.open.**
-keep class com.tencent.connect.**
-keep class com.tencent.tauth.**

#微信
-keep class com.tencent.mm.** {*;}
-keep class com.tencent.mm.sdk.openapi.WXMediaMessage {*;}
-keep class com.tencent.mm.sdk.openapi.** implements com.tencent.mm.sdk.openapi.WXMediaMessage$IMediaObject {*;}


#百度定位
-keep class com.baidu.** { *; }
-keep class vi.com.gdi.bgl.android.**{*;}
#腾讯云
-keep class com.tencent.cloudsdk.** {*; }

#universal-image-loader
-keep class com.nostra13.**

#支付宝
-keep class com.alipay.**
-keep class com.ta.**


#growingio 统计框架
-keep class com.growingio.android.sdk.** {
  public *;
}
-dontwarn com.growingio.android.sdk.**

#commons-httpclient
-keep class org.apache.** { *; }
-dontwarn org.apache.**
-dontnote org.apache.**


-dontwarn com.google.**
-keep class com.google.protobuf.**{*;}
#nineoldandroids_library.jar
-keep class com.nineoldandroids.**

-keep class com.hp.**
-keep class net.sourceforge.pinyin4j.**

-assumenosideeffects class demo.Pinyin4jAppletDemo
-dontwarn demo.Pinyin4jAppletDemo
-dontnote demo.Pinyin4jAppletDemo

#二维码Zxing.jar
#-keep class com.google.**

# ProGuard configurations for NetworkBench Lens
#-libraryjars  libs/nbs.newlens.agent.jar
-keep class com.networkbench.** { *; }
-dontwarn com.networkbench.**
-keepattributes Exceptions, Signature, InnerClasses
# End NetworkBench Lens


##---------------Begin: proguard configuration for Gson  ----------
#gson
-keep class com.google.**
# Gson specific classes
-keep class sun.misc.Unsafe { *; }
#-keep class com.google.gson.stream.** { *; }
# Application classes that will be serialized/deserialized over Gson

##---------------End: proguard configuration for Gson  ----------

#httpcore-4.1 httpmime-4.1.1
-keep class org.apache.** { *;}


##--- start greendao----
-keep class com.manjay.housebox.greendao.** {*;}
-keepclassmembers class * extends de.greenrobot.dao.AbstractDao {
    public static java.lang.String TABLENAME;
}
-keep class **$Properties

-keep class * extends java.lang.annotation.Annotation { *; }

##--- 信鸽推送----
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep class com.tencent.android.tpush.**  {* ;}
-keep class com.tencent.mid.**  {* ;}

##--- end greendao----

## 演出相关
##--- butterknife -----

-dontwarn butterknife.internal.**
-keep class **$$ViewInjector { *; }
-keepnames class * { @butterknife.InjectView *;}

##--- butterknife -----


-dontwarn android-async-http-1.4.4.jar.**
-keep class android-async-http-1.4.4.jar.**{*;}




-dontwarn retrofit2.**
-keep class retrofit2.** { *; }

-keepattributes Signature
-keepattributes Exceptions


-dontwarn okhttp3.**
-keep class okhttp3.** { *;}

-dontwarn okio.**





-dontshrink
-dontoptimize
-dontwarn com.google.android.maps.**
-dontwarn android.webkit.WebView
-dontwarn com.umeng.**
-dontwarn com.tencent.weibo.sdk.**
-dontwarn com.facebook.**


-keep enum com.facebook.**
-keepattributes Exceptions,InnerClasses,Signature
-keepattributes *Annotation*
-keepattributes SourceFile,LineNumberTable

-keep public interface com.facebook.**
-keep public interface com.tencent.**
-keep public interface com.umeng.socialize.**
-keep public interface com.umeng.socialize.sensor.**
-keep public interface com.umeng.scrshot.**

-keep public class com.umeng.socialize.* {*;}
-keep public class javax.**
-keep public class android.webkit.**

-keep class com.facebook.**
-keep class com.umeng.scrshot.**
-keep public class com.tencent.** {*;}
-keep class com.umeng.socialize.sensor.**

-keep class com.tencent.mm.sdk.modelmsg.WXMediaMessage {*;}

-keep class com.tencent.mm.sdk.modelmsg.** implements com.tencent.mm.sdk.modelmsg.WXMediaMessage$IMediaObject {*;}

-keep class im.yixin.sdk.api.YXMessage {*;}
-keep class im.yixin.sdk.api.** implements im.yixin.sdk.api.YXMessage$YXMessageData{*;}

-keep public class [your_pkg].R$*{
    public static final int *;
}