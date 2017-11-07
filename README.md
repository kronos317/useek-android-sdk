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

### 1. Adding ```USeekPlayerView```
#### Add ```USeekPlayerView``` to layout resource:
```xml
<com.useek.library_beta.USeekPlayerView
    android:id="@+id/custom_activity_useek_view"
    android:layout_width="match_parent"
    android:layout_height="match_parent" />
```
If you want to change loading placeholder text of USeekPlayerView, add follow code in **USeekPlayerViewe** xml block.
```xml
<com.useek.library_beta.USeekPlayerView
    android:id="@+id/custom_activity_useek_view"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    app:useek_loadingText="Please wait while loading..." />
```
#### Implement ```USeekPlayerView``` code in your activity.
```java
public class CustomViewSampleActivity extends AppCompatActivity {

    USeekPlayerView useekPlayerView;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_view_sample);
        
        useekPlayerView = findViewById(R.id.custom_activity_useek_view);
        useekPlayerView.setPlayerListener(this);
        
        String gameId = "{game id}";
        String userId = "{user id}";
        useekPlayerView.loadVideo(gameId, userId);
    }
 ```
 
 If you want to monitor status of video loading, implement ```USeekPlayerListener```
 ```java
 public class CustomViewSampleActivity extends AppCompatActivity implements USeekPlayerListener {
    
    ...
    
    /** USeekPlayerView listener */
    @Override
    public void useekPlayerDidFailWithError(USeekPlayerView useekPlayerView, WebResourceError error) {
        Log.d("USeek Sample", "useekPlayerDidFailWithError video");
    }

    @Override
    public void useekPlayerDidStartLoad(USeekPlayerView useekPlayerView) {
        Log.d("USeek Sample", "useekPlayerDidStartLoad video");
    }

    @Override
    public void useekPlayerDidFinishLoad(USeekPlayerView useekPlayerView) {
        Log.d("USeek Sample", "useekPlayerDidFinishLoad video");
    }

```
### 2. Adding ```USeekPlayerView``` programmatically
```java
public class CustomViewSampleActivity extends AppCompatActivity implements USeekPlayerListener {

    LinearLayout useekPlayerViewContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_view_sample);
        useekPlayerViewContainer = findViewById(R.id.useek_container);
        
        USeekPlayerView useekPlayerView = new USeekPlayerView(this);
        useekPlayerView.setLoadingTextString("{Your custom placeholder string}");
        useekPlayerViewContainer.addView(
                useekPlayerView.getView(),
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                )
        );
        useekPlayerView.setPlayerListener(this);
    }
    ...
```

### 3. Adding ```USeekPlayerFragment```

```USeekPlayerFragment``` has ```USeekPlayerCloseListener``` which is extended listener of ```USeekPlayerListener```.
```USeekPlayerCloseListener``` has ```useekPlayerDidClosed``` method to get fragment's close event.

```java
    private void showUSeekPlayerFragment() {

        USeekPlayerFragment fragment = USeekPlayerFragment.newInstance("{gameId}", "{userId}");
        fragment.setShowCloseButton(true);
        fragment.setUSeekPlayerCloseListener(new USeekPlayerCloseListener() {
            @Override
            public void useekPlayerDidClosed(USeekPlayerView useekPlayerView) {
                // removeUSeekPlayerFragment();
                Log.d("USeek Sample", "useekPlayerDidClosed");
            }

            @Override
            public void useekPlayerDidFailWithError(USeekPlayerView useekPlayerView, WebResourceError error) {
                Log.d("USeek Sample", "useekPlayerDidFailWithError video");
            }

            @Override
            public void useekPlayerDidStartLoad(USeekPlayerView useekPlayerView) {
                Log.d("USeek Sample", "useekPlayerDidStartLoad video");
            }

            @Override
            public void useekPlayerDidFinishLoad(USeekPlayerView useekPlayerView) {
                Log.d("USeek Sample", "useekPlayerDidFinishLoad video");
            }
        });
        
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_container, fragment)
                .commit();

    }
```

### 4. Adding ```USeekPlayerActivity```

```java
import static com.useek.library_beta.USeekPlayerActivity.USEEK_GAME_ID;
import static com.useek.library_beta.USeekPlayerActivity.USEEK_USER_ID;
...

    public void openUSeekPlayerActivity() {

        USeekManager.sharedInstance().setPublisherId("{publisher id}");

        USeekPlayerActivity.setUSeekPlayerCloseListener(this);
        USeekPlayerActivity.setShowCloseButton(true);
        USeekPlayerActivity.setLoadingText("Loading video...");

        Intent intent = new Intent(this, USeekPlayerActivity.class);
        intent.putExtra(USEEK_USER_ID, "{user id}");
        intent.putExtra(USEEK_GAME_ID, "{game id}");
        startActivity(intent);
    }

```
