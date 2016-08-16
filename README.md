USeek SDK
====

Three simple steps to integrate USeek SDK into your application:

1. Add new module by importing [useeksdk.aar](https://bitbucket.org/useek/useek-android-sdk/src/3f28df4f3669ba4e018c7686820e051bba5a2446/useeksdk.aar?at=master) library.
2. Go to **Project Structure** and add **useeksdk** library as a dependency of your app
3. Put this code wherever you'd like trigger USeek player:
```java
UseekSDK sdk = new UseekSDK(activity); // UseekSDK needs activity to show player  
sdk.setPublisherId("your-publisher-id"); // publisher id received from USeek
sdk.setUserId("external-user-id"); // user id from your application 
sdk.setVideoId(125); // id of video which should be played, most likely received from USeek as well
sdk.playVideo();
```

## Instructions for Android Studio ##

1. Copy [useeksdk.aar](https://bitbucket.org/useek/useek-android-sdk/src/3f28df4f3669ba4e018c7686820e051bba5a2446/useeksdk.aar?at=master) to your ```/app/libs``` directory.
2. Import the module into your app - (instructions [here](http://stackoverflow.com/a/34919810/6557231) )
3. Trigger the useek player as shown above.

## How to show earned rewards to User ##

1. Implement USeek SDK in your application as shown above
2. Use this code to show rewards:
```java
UseekSDK sdk = new UseekSDK(activity); // UseekSDK needs activity to show player  
sdk.setPublisherId("your-publisher-id"); // publisher id received from USeek
sdk.setUserId("external-user-id"); // user id from your application 
sdk.showRewards();
```
3. Some of USeek rewards may be georestricted and will require GPS location, `UseekSDK#showRewards()` method will try to send location but you have to integrate Google Play Services (see more [here](https://developers.google.com/android/guides/setup) into your application to make that work. You can do this by adding below line to your application main `build.gradle` file:
```java
compile 'com.google.android.gms:play-services-location:9.4.0'
```
