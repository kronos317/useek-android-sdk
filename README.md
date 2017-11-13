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

Minimum API level **19**, Android OS **4.4** (KitKat)


Installation
==========================

### Gradle:
Gradle version < 4.0
```groovy
compile 'com.useek:library-beta:0.0.2'
```

Gradle version >= 4.0
```groovy
implementation 'com.useek:library-beta:0.0.2'
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

There are 4 main classes
 * USeekManager
 * USeekPlayerView
 * USeekPlayerFragment
 * USeekPlayerActivity

`USeekManager` is singleton class, with which you can do the following actions
 * Set / Retrieve `publisher ID`
 * Request for the points of certain user
 
`USeekPlayerView`, `USeekPlayerFragment`, and `USeekPlayerActivity` classes are designed to easily load & play the video in any fragments / activities or layouts. You can use any of these 3 classes as per your need / use case.
Demo project demonstrates all use cases.

#### Set Publisher ID

```java
USeekManager.sharedInstance().setPublisherId("{your publisher ID}");
```

#### How to use USeekPlayerView

USeekPlayerView inherits FrameLayout, thus you can directly add it in layout resource file or add as subview programmatically.

 - Drop into layout resource file
 
```xml
<com.useek.library_beta.USeekPlayerView
    android:id="@+id/custom_activity_useek_view"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    app:useek_loadingText="Please wait while loading..." />
```

`app:useek_loadingText` paramter is optional value to change loading placeholder text.

Now you can play the video.

```java
useekPlayerView = findViewById(R.id.custom_activity_useek_view);
useekPlayerView.loadVideo("{game id}", "{user id}");
```

**Attention** : You should destroy `USeekPlayerView` when the parent activity is being dismissed. You can do this in `Activity.onStop()` method

```java
@Override
protected void onStop() {
    super.onStop();
    useekPlayerView.destroy();
}
```

 - Add as subview programmatically
 
```java
USeekPlayerView useekPlayerView = new USeekPlayerView(this);
this.mainContainer.addView(
        useekPlayerView.getView(),
        new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        )
);
```

Now you can play the video.

```java
useekPlayerView.loadVideo("{game id}", "{user id}");
```

#### USeekPlayerFragment to load video

```java

USeekPlayerFragment fragment = USeekPlayerFragment.newInstance("{your game id}", "{your user id}");
getSupportFragmentManager()
        .beginTransaction()
        .add(R.id.fragment_container, fragment)
        .commit();

```

Now you can play video.

```java
fragment.loadVideo();
```

#### USeekPlayerActivity to load video

You will need to import 2 keys.

```java
import static com.useek.library_beta.USeekPlayerActivity.USEEK_GAME_ID;
import static com.useek.library_beta.USeekPlayerActivity.USEEK_USER_ID;
```

```java
Intent intent = new Intent(this, USeekPlayerActivity.class);
intent.putExtra(USEEK_USER_ID, "{user id}");
intent.putExtra(USEEK_GAME_ID, "{game id}");
startActivity(intent);
```

Once activity is open, it will play the video automatically.

#### How to get points from server

```java
USeekManager.sharedInstance().requestPoints("{game id}", "{user id}",
        new USeekManager.RequestPointsListener() {
            @Override
            public void useekRequestForPlayPointsDidSuccess(int lastPlayPoints, int totalPlayPoints) {
            }

            @Override
            public void useekRequestForPlayPointsDidFail(Error error) {
            }
        }
);
```

## License

USeek is available under the USEEK license. See the LICENSE file for more info.
