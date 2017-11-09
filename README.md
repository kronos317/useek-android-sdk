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


### How to get points score

```java
USeekManager.sharedInstance().requestPoints("{game id}", "{user id}",
        new USeekManager.RequestPointsListener() {
            @Override
            public void didSuccess(int points) {
                Log.d("USeek Sample", String.valueOf(points);
            }

            @Override
            public void useekRequestForPlayPointsDidFail(Error error) {

            }
        }
);
```


Properties
========
### USeekPlayerView
* Set unique game id provided by USeek.
```java
public void setGameId(String gameId)
```

* Get unique game id
```java
public String getGameId()
```

* Set user's unique id registered on USeek
```java
public void setUserId(String userId)
```

* Get user's unique id registered on USeek
```java
public String getUserId()
```

* Set listener for video loading status
```java
public void setPlayerListener(USeekPlayerListener playerListener)
```

* Load Video with existing GameId and UserId
```java
public void loadVideo()
```

* Load Video with GameId and UserId
```java
public void loadVideo(String gameId, String userId)
```

* Set placeholder text for loading video
```java
public void setLoadingTextString(String loadingTextString)
```

* Create USeekPlayerView instance
```java
public View getView()
```


### USeekPlayerFragment
* Set showing option of close button
```java
public void setShowCloseButton(boolean showCloseButton)
```

* Set placeholder text for loading video
```java
public void setLoadingText(String loadingText)
```

* Set ```USeekPlayerCloseListener``` to monitor status of loading video like as start, completed, failed and close fragment
```java
public void setUSeekPlayerCloseListener(USeekPlayerCloseListener listener)
```

### USeekPlayerActivity

_**USeekPlayerActivity use static methods to set properties**_

* **CONSTANT** for parameter key of User ID
```java
public static final String USEEK_USER_ID = "userId";
```

* **CONSTANT** for parameter key of Game ID
```java
public static final String USEEK_GAME_ID = "gameId";
```

* Set ```USeekPlayerCloseListener``` to monitor status of loading video like as start, completed, failed and close activity
```java
public static void setUSeekPlayerCloseListener(USeekPlayerCloseListener listener)
```

* Set showing option of close button
```java
public static void setShowCloseButton(boolean showCloseButton)
```

* Set placeholder text for loading video
```java
public static void setLoadingText(String loadingText)
```

Listener
=======

### USeekPlayerListener
* Called when player detected an error while loading the video.
```java
void useekPlayerDidFailWithError(USeekPlayerView useekPlayerView, WebResourceError error);
```

* Called when player did start loading the video.
```java
void useekPlayerDidStartLoad(USeekPlayerView useekPlayerView);
```

* Called when player did finish loading the video.
```java
void useekPlayerDidFinishLoad(USeekPlayerView useekPlayerView);
```

### USeekPlayerCloseListener
```USeekPlayerCloseListener``` is sub interface of ```USeekPlayerListener```.

* Called when user clicked close button to dismiss the Activity or Fragment.
```java
void useekPlayerDidClosed(USeekPlayerView useekPlayerView);
```

