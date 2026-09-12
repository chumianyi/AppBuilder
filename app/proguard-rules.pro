# ProGuard rules
-keep class com.mian.appbuilder.model.** { *; }
-keep class * extends androidx.room.RoomDatabase { *; }
-dontwarn org.jetbrains.annotations.**
