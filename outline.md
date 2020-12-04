
## Week 1
### 08/10/2020 - Course introduction
* Syllabus, teaching methodology and bibliography.
  * Evaluation
  * Resources

For reference:
  * [Lecture video](https://www.youtube.com/watch?v=rBZZltKF_bM&list=PL8XxoCaL3dBgFivHBJ0WXiAjNCKq3pu2w&index=1)
  
## Week 2
### 12/10/2020 - Hello Android
* Android application development: introduction
  * [Inversion of Control](https://martinfowler.com/bliki/InversionOfControl.html)
  * [Activity](https://developer.android.com/guide/components/activities/intro-activities)
    * [Lifecycle](https://developer.android.com/guide/components/activities/activity-lifecycle)
    * Specifying its UI through a resource [layout](https://developer.android.com/guide/topics/resources/layout-resource)
  * [Application resources](https://developer.android.com/guide/topics/resources/providing-resources)
    * Language dependent: [localization](https://developer.android.com/guide/topics/resources/localization)
* [The Kotlin programming language](https://kotlinlang.org/docs/reference/)
  * Main characteristics
  

For reference:
  * [Lecture video](https://www.youtube.com/watch?v=28mujldiGhg&list=PL8XxoCaL3dBgFivHBJ0WXiAjNCKq3pu2w&index=2)

### 15/10/2020 - Building Android applications
* Goal: Build a replica of the [Memory Matrix game](https://www.lumosity.com/en/brain-games/memory-matrix/)
* Initial design considerations
  * The [Model-View-Controller](https://web.archive.org/web/20120729161926/http://st-www.cs.illinois.edu/users/smarch/st-docs/mvc.html) design pattern: origins and motivation
* [Building a UI in Android](https://developer.android.com/guide/topics/ui)
  * The Android View hierarchy (overview)
    * Views
    * [Layouts](https://developer.android.com/guide/topics/ui/declaring-layout)
  * [Constraint Layout](https://developer.android.com/training/constraint-layout/)

For reference:
  * [TilePanel library](assets/Tiles.zip), by [Pedro Pereira](https://www.linkedin.com/in/palexpereira/)
  * [Lecture video]([#15102020---building-android-applications](https://www.youtube.com/watch?v=oFFLaQBo9uM&list=PL8XxoCaL3dBgFivHBJ0WXiAjNCKq3pu2w&index=3))

### 19/10/2020 - Building a UI
* Goal: Build a replica of the [Memory Matrix game](https://www.lumosity.com/en/brain-games/memory-matrix/)
* Building a UI
  * Starting by building the model to get acquainted with Koltin: live coding session

For reference:
  * [Android KTX](https://developer.android.com/kotlin/ktx)
  * Kotlin constructs
    * [SAM conversions](https://kotlinlang.org/docs/reference/java-interop.html#sam-conversions)
    * [Nullable types](https://kotlinlang.org/docs/reference/null-safety.html)
    * [Data classes](https://kotlinlang.org/docs/reference/data-classes.html)
    * [Extension functions](https://kotlinlang.org/docs/reference/extensions.html)
    * [Companion objects](https://kotlinlang.org/docs/reference/object-declarations.html#companion-objects)
  * [Lecture video]([#19102020---building-a-ui](https://www.youtube.com/watch?v=ETfwr0x8m90&list=PL8XxoCaL3dBgFivHBJ0WXiAjNCKq3pu2w&index=4))

### 22/10/2020 - Building a UI, continued
* Goal: Build a replica of the [Memory Matrix game](https://www.lumosity.com/en/brain-games/memory-matrix/)
* Building a UI
  * Threading model: introduction
  * [Custom Views](https://developer.android.com/guide/topics/ui/custom-components)
  * [Rendering](https://developer.android.com/guide/topics/ui/how-android-draws)

For reference:
  * [Lecture video](https://www.youtube.com/watch?v=fp-JUis6peo&list=PL8XxoCaL3dBgFivHBJ0WXiAjNCKq3pu2w&index=5)

### 26/10/2020 - Building a UI, continued
* Goal: Build a replica of the [Memory Matrix game](https://www.lumosity.com/en/brain-games/memory-matrix/)
* Building a UI, continued
  * UI Event model
    * [Handling touch events](https://developer.android.com/training/gestures/movement)
  * [View Binding](https://developer.android.com/topic/libraries/view-binding)
* Activity lifecycle, revisited
  * Activity recreation due to configuration changes
    * Motivation
    * Consequences (introduction)

For reference:
  * [Lecture video](https://www.youtube.com/watch?v=XK_VSVwkFuA&list=PL8XxoCaL3dBgFivHBJ0WXiAjNCKq3pu2w&index=6)

### 02/11/2020 - Presentation state (UI state)
 * Activity lifecycle, revisited
   * Activity recreation due to configuration changes
     * Motivation and consequences, revisited
 * Characterizing state
   * [UI state](https://developer.android.com/topic/libraries/architecture/saving-states) and application state: introduction
 * Considerations on the architecture of Android applications
   * GUI Design Patterns
     * _Model - View - Controller_ ([MVC](https://web.archive.org/web/20120729161926/http://st-www.cs.illinois.edu/users/smarch/st-docs/mvc.html))
     * _Model - View - ViewModel_ ([MVVM](https://docs.microsoft.com/en-us/previous-versions/msp-n-p/hh848246(v=pandp.10)))
   * Elements of [Android application architecture](https://developer.android.com/jetpack/guide)
     * The [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel)
 
For reference:
  * [Kotlin's delegated properties](https://kotlinlang.org/docs/reference/delegated-properties.html)
  * [Jetpack lifecycle dependencies](https://developer.android.com/jetpack/androidx/releases/lifecycle#kotlin)
  * [Kotlin Activity extensions](https://developer.android.com/jetpack/androidx/releases/activity)
  * [Lecture video](https://www.youtube.com/watch?v=eyDWLOxTaWE&list=PL8XxoCaL3dBgFivHBJ0WXiAjNCKq3pu2w&index=7)

### 05/11/2020 - Presentation state (UI state), continued
* Preserving UI state across system initiated process terminations
  * Activity lifecycle, revisited
    * `onCreate()`, `onSaveInstanceState()` and `onRestoreInstanceState()`
  * [`Parcelable` contract](https://developer.android.com/reference/android/os/Parcelable)
    * Motivation, revisited
    * Manual implementation 
    * Implementation with `@Parcelize` [(a Kotlin extension)](https://kotlinlang.org/docs/reference/compiler-plugins.html#parcelable-implementations-generator)
  * [Saved State module for ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel-savedstate)

For reference: 
  * [Lecture video](https://www.youtube.com/watch?v=vfoFneVDWdI&list=PL8XxoCaL3dBgFivHBJ0WXiAjNCKq3pu2w&index=8)
  * [ADB documentation](https://developer.android.com/studio/command-line/adb#shellcommands)
    * [Activity Manager](https://developer.android.com/studio/command-line/adb#am)

### 09/11/2020 - Practical class
Practical class dedicated to the development of the first part of the course's project (requirements are [here](assets/PDM-2021-1_Trab1.pdf), in Portuguese)

### 12/11/2020 - Building Android applications, continued
* Considerations on the design of Android applications, revisited
* Navigation between activities
  * [Intents (explicit and implicit) and intent filters](https://developer.android.com/guide/components/intents-filters)
  * [User tasks and back stack](https://developer.android.com/guide/components/activities/tasks-and-back-stack)
  * [Activity life cycle, revisited](https://developer.android.com/guide/components/activities/activity-lifecycle)
    * `onStop()`, `onStart()` and `onRestart()`
* Android application components
  * [Application](https://developer.android.com/reference/android/app/Application)
    * Motivation
    * Lifecycle

For reference: 
  * [Lecture video](https://www.youtube.com/watch?v=wblbeUBol1Q&list=PL8XxoCaL3dBgFivHBJ0WXiAjNCKq3pu2w&index=9)
  * [Manifest file elements: activity](https://developer.android.com/guide/topics/manifest/activity-element#screen)
  * [Manifest file elements: application](https://developer.android.com/guide/topics/manifest/application-element)
  * [Menus](https://developer.android.com/guide/topics/ui/menus)
  * [Menu resource](https://developer.android.com/guide/topics/resources/menu-resource)

### 16/11/2020 - Building Android applications, continued
* Elements of [Android application architecture](https://developer.android.com/jetpack/guide), continued
   * [LiveData](https://developer.android.com/topic/libraries/architecture/livedata)
     * Motivation and challenges
* Navigation between activities, continued
  * [Implicit intents and intent filters](https://developer.android.com/guide/components/intents-filters)
  * [Common intents](https://developer.android.com/guide/components/intents-common)
  * [Navigating to a URL via the browser](https://developer.android.com/guide/components/intents-common#Browser)

For reference: 
  * [Lecture video](https://www.youtube.com/watch?v=AGvnYpUhelk&list=PL8XxoCaL3dBgFivHBJ0WXiAjNCKq3pu2w&index=10)
  * [Package visibility in Android 11](https://developer.android.com/about/versions/11/privacy/package-visibility)
  * Brand links:
    * [Twitch](https://brand.twitch.tv/)
    * [YouTube](https://www.youtube.com/about/brand-resources/#logos-icons-colors)
    * [LinkedIn](https://brand.linkedin.com/downloads)

### 19/11/2020 - Practical class
Practical class dedicated to the development of the first part of the course's project (requirements are [here](assets/PDM-2021-1_Trab1.pdf), in Portuguese)

### 23/11/2020 - Building Android applications, continued
* Persisting application state
  * In a key-value store
    * [Shared Preferences](https://developer.android.com/training/data-storage/shared-preferences)
  * In a relational DB with [Room](https://developer.android.com/training/data-storage/room#kotlin)
    * Architectural elements: [Entities](https://developer.android.com/training/data-storage/room/defining-data), [DAOs](https://developer.android.com/training/data-storage/room/accessing-data) and Database
* [Android threading model](https://developer.android.com/guide/components/processes-and-threads)
  * Thread safety through thread confinement
    * Motivation and consequences
  * Scheduling work (e.g. I/O) to alternative threads
    * Executors
    * [Asynctask (deprecated in Android 11)](https://developer.android.com/reference/android/os/AsyncTask.html)
  * LiveData as a means to signal asynchronous operations completion

For reference:
* [Lecture video](https://www.youtube.com/watch?v=5Kp3KahrhIo&list=PL8XxoCaL3dBgFivHBJ0WXiAjNCKq3pu2w&index=11)
* [Saving data using SQLite](https://developer.android.com/training/data-storage/sqlite)
* Coming soon: [DataStore](https://developer.android.com/topic/libraries/architecture/datastore#typed-datastore)

### 26/11/2020 - Building the UI, continued
* Displaying lists of items
  * Adapting data to views (Adapter pattern)
  * Using ListView
    * View Holder pattern 
    * View recycling
  * [Using Recycler View](https://developer.android.com/guide/topics/ui/layout/recyclerview)

For reference:
* [Lecture video](https://www.youtube.com/watch?v=XkIZXpsvUtM&list=PL8XxoCaL3dBgFivHBJ0WXiAjNCKq3pu2w&index=12)

### 03/12/2020 - Networking (with Volley)
* Accessing the communications network
  * [Required permissions](https://developer.android.com/training/basics/network-ops/connecting)
* [HTTP communication with Volley](https://developer.android.com/training/volley)
* Serialization and deserialization with Jackson
* Further considerations regarding Android application architecture
  * [Android application architecture](https://developer.android.com/jetpack/guide), revisited

For reference:
* [Lecture video](#03122020---networking-with-volley)

### 11/12/2020 - Networking, continued (preview)
* [HTTP communication with Retrofit](https://square.github.io/retrofit/)
* Further considerations regarding Android application architecture
  * [Android application architecture](https://developer.android.com/jetpack/guide), revisited