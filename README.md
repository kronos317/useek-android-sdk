<p align="center">
<img src="https://static1.squarespace.com/static/592df079893fc0e042b0e585/t/592df28c3e00be8e7a34d733/1498102982326/?format=1500w" alt="Icon"/>
</p>

[![License](https://img.shields.io/cocoapods/l/USeek.svg?style=flat)](http://cocoapods.org/pods/USeek)

## What is USeek?

The USeek interactive advertising solution turns your videos into engaging experiences full of rewards...

[![Rewards](https://static1.squarespace.com/static/592df079893fc0e042b0e585/t/59496cddbe6594e7cda66c6a/1497984245701/?format=1500w)](https://www.landing.useek.com/)

...Increasing viewer attention while searching your creative for objects.

## USeek Library

`USeek` library is designed to help developers easily add the enjoyable features of USeek in their own application.

## Warning

- **USeek** utilizes the power of `web browser`'s interactive video feature, and this is only available from ??? or later. 

## Requirements

[![Platform Android](https://img.shields.io/badge/Platform-Android-blue.svg?style=fla)]()

#### USeek:-
[![Java](https://img.shields.io/badge/Language-Java-blue.svg?style=flat)](https://java.com/en/)

Minimum API version : **15**

Development Environment : **Android Studio 3.0**

Installation
==========================

### Gradle:
Gradle version < 3.0
```groovy
compile 'com.useek:library-beta:0.0.1'
```

Gradle version >= 3.0
```groovy
implementation 'com.useek:library-beta:0.0.1'
```
### Maven:
```xml
<dependency>
  <groupId>com.useek</groupId>
  <artifactId>library-beta</artifactId>
  <version>0.0.1</version>
  <type>pom</type>
</dependency>
```

Usage
--------
##### You should set ```publisher ID``` provided by USeek on ```Application``` or ```MainActivity```.
```java
USeekManager.sharedInstance().setPublisherId("{your publisher id}");
```
##### Also you should set ```game id``` and ```user id``` when play video
```java
useekPlayerView.loadVideo(gameId, userId);
```

There are some usage methods of USeek SDK.

### Adding ```USeekPlayerView```
#### Add ```USeekPlayerView``` to layout resource:
```xml
<com.useek.library_beta.USeekPlayerView
    android:id="@+id/custom_activity_useek_view"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    app:useek_loadingText="Please wait while loading..." />
```
```app:useek_loadingText``` paramter is optional value to change loading placeholder text.

#### Implement ```USeekPlayerView``` code in your activity.
```java
    useekPlayerView = findViewById(R.id.custom_activity_useek_view);
    useekPlayerView.setPlayerListener(this);        
    useekPlayerView.loadVideo("{game id}", "{user id}");
 ```
 
### Adding ```USeekPlayerView``` without use layout resource.
```java
    USeekPlayerView useekPlayerView = new USeekPlayerView(this);
    useekPlayerViewContainer.addView(
            useekPlayerView.getView(),
            new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
            )
    );
```

### Adding ```USeekPlayerFragment```

```java

    USeekPlayerFragment fragment = USeekPlayerFragment.newInstance("{gameId}", "{userId}");
    getSupportFragmentManager()
            .beginTransaction()
            .add(R.id.fragment_container, fragment)
            .commit();

```

### Adding ```USeekPlayerActivity```

```java
import static com.useek.library_beta.USeekPlayerActivity.USEEK_GAME_ID;
import static com.useek.library_beta.USeekPlayerActivity.USEEK_USER_ID;
...

    public void openUSeekPlayerActivity() {

        Intent intent = new Intent(this, USeekPlayerActivity.class);
        intent.putExtra(USEEK_USER_ID, "{user id}");
        intent.putExtra(USEEK_GAME_ID, "{game id}");
        startActivity(intent);
    }

```
