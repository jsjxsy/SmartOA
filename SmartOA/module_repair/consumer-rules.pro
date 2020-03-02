-optimizationpasses 5
 -dontusemixedcaseclassnames
 -dontskipnonpubliclibraryclasses
 -dontpreverify
 -verbose
 -keepattributes Exceptions,InnerClasses,Signature,Deprecated,SourceFile,LineNumberTable,*Annotation*,EnclosingMethod
 -optimizations !code/simplification/arithmetic,!field/*,!class/merging/*
 -dontwarn javax.annotation.Nullable

 -keep public class * extends android.app.Activity
 -keep public class * extends android.app.Application
 -keep public class * extends android.app.Service
 -keep class * extends android.content.BroadcastReceiver
 -keep public class * extends android.content.ContentProvider
 -keep public class * extends android.app.backup.BackupAgentHelper
 -keep public class * extends android.preference.Preference
 -keep public class * extends android.view.View
 -keep public class * extends android.os.IInterface
 -keep class android.media.* { *; }
 -keep public class com.android.internal.telephony.* { *; }
 -keep public class android.os.storage.* { *; }
 -keep public class android.content.pm.* { *; }
 -keep public class * extends android.support.v4.app.Fragment
 -keep public class * extends android.app.Fragment
 -keep class com.tencent.stat.*{*;}
 -keep class com.tencent.mid.*{*;}
 -keep public class com.alibaba.android.arouter.routes.**{*;}
 -keep class * implements com.alibaba.android.arouter.facade.template.ISyringe{*;}

 -keepclasseswithmembernames class * {
     native <methods>;
 }

 -keep class *.R

 -keepclasseswithmembers class **.R$* {
     public static <fields>;
 }

 -keepclasseswithmembers class * {
     public <init>(android.content.Context, android.util.AttributeSet);
 }

 -keepclasseswithmembers class * {
     public <init>(android.content.Context, android.util.AttributeSet, int);
 }
 -keepclassmembers enum * {
     public static **[] values();
     public static ** valueOf(java.lang.String);
 }

 -keep class * implements android.os.Parcelable {
   public static final android.os.Parcelable$Creator *;
 }

 -keepclassmembers class * {
    public <init>(org.json.JSONObject);
 }

-dontwarn !com.xxx.xxx.**
-keep class !com.xxx.xxx.** { *; }
-ignorewarnings