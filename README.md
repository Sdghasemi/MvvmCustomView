# Peaks Assignment App
This is a sample app using a fictional endpoint to retrieve rectangles properties and display them in a custom view.

The app is written in pure Kotlin and is using Kotlin Gradle DSL for gradle scripts as well.

MainFragment is responsible for displaying the rectangles and enabling users to move them.

Data is being cached in database and constantly updated on user interactions.

If there was an error retrieving data, a relevant message would inform the user about the situation.

## Features
The application reaches the endpoint and caches the response. The cache expires after 1 week of inactivity.
When the cache is ready and valid (not expired) no endpoint call is made and the UI is populated purely with the cache itself.

The images displayed as thumbnails are also cached for better experience.

## Libraries
<ul>
<li>Basic test and AppCompat libraries such as: JUnit, Robolectric, Espresso, Google Material, ConstraintLayout, Lifecycle (LiveData and ViewModels), Coroutines, etc.</li>
<li>Retrofit and OkHttp for network calls</li>
<li>Room for caching and Database manipulation</li>
</ul>

## Decisions
In order to draw rectangles, either using paint or declaring a drawable would do the job. Also the performance was almost the same.
I decided to use the drawable to enable the developer for better future changes and easier control over the shape of the rectangle.

The base API url of the endpoint was not clear from the assignment, so I asked Tjerk and turns out the API is fictional so
I decided to use "https://example.com/".

### Contact developer

If there's ***anything*** you'd like to discuss, feel free to contact me at [Sd.ghasemi1@gmail.com](mailto:Sd.ghasemi1@gmail.com).

Cheers🍻