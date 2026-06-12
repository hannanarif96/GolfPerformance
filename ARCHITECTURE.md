# GolfPerformance Architecture and Design Decisions

## Overview

GolfPerformance is an Android application developed using the MVVM (Model-View-ViewModel) architectural pattern. The application consumes remote APIs to display golf player information and shot performance data while maintaining a clear separation between the presentation, business, and data layers.

## Architecture

The application is structured into the following layers:

### UI Layer

The UI layer consists of Activities, Fragments, and adapters responsible for displaying data and handling user interactions. The UI observes state exposed by ViewModels and does not directly communicate with data sources.

### ViewModel Layer

ViewModels manage UI state and coordinate interactions between the UI and repository layers. Kotlin Coroutines and StateFlow are used to handle asynchronous operations and state updates.

### Data Layer

The data layer contains repositories and API services. Repositories act as the single source of truth for the application and abstract the underlying data source implementation from the rest of the application.

### Network Layer

Retrofit is used to communicate with remote APIs. Data Transfer Objects (DTOs) received from the API are mapped into domain models used throughout the application.

## Design Decisions

### MVVM Architecture

MVVM was selected because it promotes separation of concerns, improves maintainability, and enables easier testing of business logic.

### Repository Pattern

Repositories provide a clean abstraction over data retrieval and make it easier to extend the application with additional data sources such as local caching in the future.

### Kotlin Coroutines and Flow

Coroutines and StateFlow were chosen to simplify asynchronous programming and provide reactive UI updates.

### Testability

The architecture was designed to allow ViewModels and repositories to be unit tested independently by mocking dependencies.

### Scalability

The layered structure makes it straightforward to add new features, data sources, or API endpoints without impacting existing functionality.

## Screenshots

### Player List and Details Screen

![Player List](screenshots/player_list.png)

### Player List and Details Screen (Search)

![Player List (Search)](screenshots/player_list_search.png)

### Player Shots Screen

![Player Shot Details](screenshots/player_shots.png)

### Shot Performance Trends Screen

![Performance Trends](screenshots/shot_performance_trends.png)

## Conclusion

The application follows modern Android development practices with a focus on maintainability, testability, and scalability. The chosen architecture provides a solid foundation for future enhancements while keeping responsibilities clearly separated across application layers.
