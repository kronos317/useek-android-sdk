## Requirements

[![Platform Android](https://img.shields.io/badge/Platform-Android-blue.svg?style=fla)]()

#### USeek:-
[![Java](https://img.shields.io/badge/Language-Java-blue.svg?style=flat)](https://java.com/en/)
[![Java](https://img.shields.io/badge/Language-Kotlin-red.svg?style=flat)](https://kotlinlang.org/)

Minimum API level **19**, Android OS **4.4** (KitKat)

Installation
==========================
This library is prepared for both of Java and Kotlin.

### Gradle:
##### Java library
```groovy
implementation 'com.useek:library-beta:0.0.2'
```
##### Kotlin library
```groovy
implementation 'com.useek:library-kt-beta:0.0.2'
```

### Maven:
##### Java library
```xml
<dependency>
  <groupId>com.useek</groupId>
  <artifactId>library-beta</artifactId>
  <version>0.0.2</version>
  <type>pom</type>
</dependency>
```
##### Kotlin library
```xml
<dependency>
  <groupId>com.useek</groupId>
  <artifactId>library-kt-beta</artifactId>
  <version>0.0.2</version>
  <type>pom</type>
</dependency>
```

Usage
--------

There are 4 main classes
 - USeekManager
 - USeekPlayerView
 - USeekPlayerFragment
 - USeekPlayerActivity

`USeekManager` is singleton class, with which you can do the following actions
 - Set / Retrieve `publisher ID`
 - Request for the points of certain user
 
`USeekPlayerView`, `USeekPlayerFragment`, and `USeekPlayerActivity` classes are designed to easily load & play the video in any fragments / activities or layouts. You can use any of these 3 classes as per your need / use case.
Demo project demonstrates all use cases.

**NOTE** : We describe all use case of Java and Kotlin libraries to each other language.

### Set Publisher ID
##### Java library in Java code
```java
USeekManager.sharedInstance().setPublisherId("{your publisher ID}");
```
##### Java library in Kotlin code
```kotlin
USeekManager.sharedInstance().publisherId = "{your publisher ID}"
```
##### Kotlin library in Java code
```java
USeekManager.Companion.getSharedInstance().setPublisherId("{your publisher ID}");
```
##### Kotlin library in Kotlin code
```kotlin
USeekManager.sharedInstance.publisherId = "{your publisher ID}"
```

### How to use USeekPlayerView

USeekPlayerView inherits FrameLayout, thus you can directly add it in layout resource file or add as subview programmatically.

 - Drop into layout resource file
 
```xml
<com.useek.library_beta.USeekPlayerView
    android:id="@+id/useekPlayerView"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    app:useek_loadingText="Please wait while loading..." />
```

`app:useek_loadingText` paramter is optional value to change loading placeholder text.

Now you can play the video.

##### Java
```java
useekPlayerView = findViewById(R.id.useekPlayerView);
useekPlayerView.loadVideo("{game id}", "{user id}");
```
##### Kotlin
```kotlin
useekPlayerView.loadVideo("{game id}", "{user id}")
```

**Attention** : You should destroy `USeekPlayerView` when the parent activity is being dismissed. You can do this in `Activity.onStop()` method

##### Java
```java
@Override
protected void onStop() {
    super.onStop();
    useekPlayerView.destroy();
}
```
##### Kotlin
```kotlin
override fun onStop() {
    super.onStop()
    useekPlayerView.destroy()
}
```

 - Add as subview programmatically
 
##### Java
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
##### Kotlin
```kotlin
val useekPlayerView = USeekPlayerView(this)
mainContainer.addView(
        useekPlayerView.view,
        LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        )
)
```

Now you can play the video.

##### Java
```java
useekPlayerView.loadVideo("{game id}", "{user id}");
```
##### Kotlin
```kotlin
useekPlayerView.loadVideo("{game id}", "{user id}")
```

### USeekPlayerFragment to load video

##### Java
```java

USeekPlayerFragment fragment = USeekPlayerFragment.newInstance("{your game id}", "{your user id}");
getSupportFragmentManager()
        .beginTransaction()
        .add(R.id.fragment_container, fragment)
        .commit();

```
##### Kotlin
```kotlin

val fragment = USeekPlayerFragment.newInstance("{your game id}", "{your user id}")
supportFragmentManager
        .beginTransaction()
        .add(R.id.fragmentContainer, fragment)
        .commit()

```

Now you can play video.

##### Java
```java
fragment.loadVideo();
```
##### Kotlin
```kotlin
fragment.loadVideo()
```

### USeekPlayerActivity to load video

You will need to import 2 keys.

##### Java library in Java code
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
##### Java library in Kotlin code
```kotlin
import com.useek.library_beta.USeekPlayerActivity.USEEK_GAME_ID
import com.useek.library_beta.USeekPlayerActivity.USEEK_USER_ID
```

```kotlin
val intent = Intent(this, USeekPlayerActivity::class.java)
intent.putExtra(USEEK_USER_ID, "{user id}")
intent.putExtra(USEEK_GAME_ID, "{game id}")
startActivity(intent)
```
##### Kotlin library in Java code
```java
import com.useek.library_kt_beta.USeekPlayerActivity;
```

```java
Intent intent = new Intent(this, USeekPlayerActivity.class);
intent.putExtra(USeekPlayerActivity.Companion.getUSEEK_USER_ID(), "{user id}");
intent.putExtra(USeekPlayerActivity.Companion.getUSEEK_GAME_ID(), "{game id}");
startActivity(intent);
```
##### Kotlin library in Kotlin code
```kotlin
import com.useek.library_kt_beta.USeekPlayerActivity.Companion.USEEK_GAME_ID
import com.useek.library_kt_beta.USeekPlayerActivity.Companion.USEEK_USER_ID
```

```kotlin
val intent = Intent(this, USeekPlayerActivity::class.java)
intent.putExtra(USEEK_USER_ID, "{user id}")
intent.putExtra(USEEK_GAME_ID, "{game id}")
startActivity(intent)
```

Once activity is open, it will play the video automatically.

### How to get points from server

##### Java library in Java code
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
##### Java library in Kotlin code
```kotlin
USeekManager.sharedInstance().requestPoints("{game id}", "{user id}",
      object : USeekManager.RequestPointsListener {
          override fun useekRequestForPlayPointsDidSuccess(lastPlayPoints: Int, totalPlayPoints: Int) {
          }

          override fun useekRequestForPlayPointsDidFail(error: Error?) {
          }
      }
)
```
##### Kotlin library in Java code
```java
Function3<? super Integer, ? super Integer, ? super Error, Unit> requestPointLambda =
        new Function3<Integer, Integer, Error, Unit>() {
            @Override
            public Unit invoke(Integer lastPlayPoints, Integer totalPlayPoints, Error error) {
            
                return null;
            }
        };
        
USeekManager.Companion.getSharedInstance().requestPoints("{game id}", "{user id}", requestPointLambda);
```
##### Kotlin library in Kotlin code
```kotlin
USeekManager.sharedInstance.requestPoints("{game id}", "{user id}") { lastPlayPoints, totalPlayPoints, error ->
    if (error == null) {
        ...
    } else {
        ...
    }
}
```
