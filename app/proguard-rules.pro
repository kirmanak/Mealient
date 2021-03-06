# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

### Keep Protobuf classes https://github.com/protocolbuffers/protobuf/issues/6463#issuecomment-632884075 ###
-keep class * extends com.google.protobuf.GeneratedMessageLite { *; }
### Keep Protobuf classes https://github.com/protocolbuffers/protobuf/issues/6463#issuecomment-632884075 ###

### Remove logging https://www.guardsquare.com/manual/configuration/examples#logging ###
-assumenosideeffects class android.util.Log {
    public static int v(...);
    public static int i(...);
    public static int w(...);
    public static int d(...);
    public static int e(...);
}
### Remove logging https://www.guardsquare.com/manual/configuration/examples#logging ###

### kotlinx.serialization https://github.com/Kotlin/kotlinx.serialization#android ###
-if @kotlinx.serialization.Serializable class **
-keepclassmembers class <1> {
    static <1>$Companion Companion;
}
-if @kotlinx.serialization.Serializable class ** {
    static **$* *;
}
-keepclassmembers class <1>$<3> {
    kotlinx.serialization.KSerializer serializer(...);
}
-if @kotlinx.serialization.Serializable class ** {
    public static ** INSTANCE;
}
-keepclassmembers class <1> {
    public static <1> INSTANCE;
    kotlinx.serialization.KSerializer serializer(...);
}
-keepattributes RuntimeVisibleAnnotations,AnnotationDefault
### kotlinx.serialization https://github.com/Kotlin/kotlinx.serialization#android ###

### OkHttp warnings https://github.com/square/okhttp/issues/6258 ###
-dontwarn okhttp3.internal.platform.**
-dontwarn org.conscrypt.**
-dontwarn org.bouncycastle.**
-dontwarn org.openjsse.**
### OkHttp warnings ###

### Glide https://bumptech.github.io/glide/doc/download-setup.html#proguard ###
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.module.AppGlideModule
-keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}
### Glide https://bumptech.github.io/glide/doc/download-setup.html#proguard ###