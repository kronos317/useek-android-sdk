USeek SDK
====

Three simple steps to integrate USeek SDK into your application:

1. Add new module by importing [useeksdk.aar](https://bitbucket.org/useek/useek-android-sdk/src/3f28df4f3669ba4e018c7686820e051bba5a2446/useeksdk.aar?at=master) library.
2. Go to **Project Structure** and add **useeksdk** library as a dependency of your app
3. Put this code wherever you'd like trigger USeek player:
```java
UseekSDK sdk = new UseekSDK(context); // UseekSDK needs appplication context to show player  
sdk.setPublisherId("your-publisher-id"); // publisher id received from USeek
sdk.setUserId("external-user-id"); // user id from your application 
sdk.setVideoId(125); // id of video which should be played, most likely received from USeek as well
sdk.playVideo();
```

## Instructions for Android Studio ##

1. Copy [useeksdk.aar](https://bitbucket.org/useek/useek-android-sdk/src/3f28df4f3669ba4e018c7686820e051bba5a2446/useeksdk.aar?at=master) to your ```/app/libs``` directory.
2. Import the module into your app - (instructions [here](http://stackoverflow.com/a/34919810/6557231) )
3. Trigger the useek player as shown above.