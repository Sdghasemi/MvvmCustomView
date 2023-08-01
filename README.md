# Rectangles Custom View
This is a sample app using a fictional endpoint to retrieve rectangles properties and display them in a custom view.

Rectangles are placed around the screen based on their size and coordinates. User can drag them around the screen and update cached their position.

The app is written in pure Kotlin and is using Kotlin Gradle DSL for gradle scripts as well.

MainFragment is responsible for displaying the rectangles and enabling users to move them.

Data is being cached in database and constantly updated on user interactions.

If there was an error retrieving data, a relevant message would inform the user about the situation.

## Features
The application reaches the endpoint and caches the response. The cache expires after 1 week of inactivity.
When the cache is ready and valid (not expired) no endpoint call is made and the UI is populated purely with the cache itself.

## Architecture
Built with MVVM clean code.

## Libraries
<ul>
<li>Basic test and AppCompat libraries such as: JUnit, Robolectric, Espresso, Google Material, ConstraintLayout, Lifecycle (LiveData and ViewModels), Coroutines, etc.</li>
<li>Retrofit and OkHttp for network calls</li>
<li>Room for caching and Database manipulation</li>
</ul>

## Testing
- #### Unit Testing
  - Contains ViewModel tests with fake repository
- #### UI Testing
  - Main fragment internet connectivity test and rectangles user interactions included

### API
```
  GET /resourcer/v1/rectangles HTTP/1.1

  HTTP/1.1 200 OK
  Content-Type: application/json
  {
    "rectangles": [
      {
        "x": 0.5,
        "y": 0.5,
        "size": 0.2
      },
      {
        "x": 0.7,
        "y": 0.7,
        "size": 0.2
      }
    ]
  }
```
- `rectangles`: list of rectangles.
- `x` and `y`: position of the rectangle relative to the screen. E.g. x:0.5, y:0.5 means a
  rectangle in the centre of the screen.
- `size`: the size of the rectangle in percentage relative to the screen. E.g. size:0.1
  means a width of 10% of the width of the screen and a height of 10% of the height of
  the screen.

## APK
A [pre-built APK](app-debug.apk) is placed in the root of the project for your convenience.

### Contact developer

If there's ***anything*** you'd like to discuss, feel free to contact me at [Sd.ghasemi1@gmail.com](mailto:Sd.ghasemi1@gmail.com).

Cheers🍻