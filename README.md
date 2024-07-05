Currency converter app which allows to convert currencies.

Tech stack used:

1. Kotlin
2. Jetpack Compose for UI layer
3. Retrofit and Okhttp for network communication
4. Kotlin serialization for parsing json response
5. Hilt as DI framework
6. Room and Data Store for local persistence
7. Mockk for unit tests
8. MVVM architecture using Jetpack ViewModel
9. Kotlin coroutines for multithreading

Application is modularized by features with shared functionality declared as "core" modules such as access to user's balance or design system tokens to use for UI and could be represented as following:
```
                             App
                              |
                              |
        ----------------------------------------------
        |                     |                      |
        |                     |                      |
core:design-system       core:balance          core:exchange
        |                     |                      |
        |                     |                      |
        ----------------------------------------------
                              |
                              |
                       feature:exchange
```

Some of the core functionality classes are covered with unit tests such as ExchangeEngine, FeeResolver and BalanceLocalDataSource which could be found in core:exchange and core:balance modules.

Since app uses local persistence there is "Reset" button in the top app bar to clear locally stored data for convenience.