The intent of this project is to showcase the capabilities of using the  
latest tools and technologies for developing an Android application, using  
a clean architecture.

**Architecture**

Clean design of MVVM design pattern was used.  
Single activity design hosting many fragments.  
One way and two way databinding between Views and their ViewModels.  
Navigation component is used for navigation between different screens.

**Network Layer**

Retrofit + OkHttp  
Capture the bearer token unauthenticated calls.  
Inject the token into the header of authenticated tokens.  
Detect when the used token is expired, and broadcast this event.  
MainActivity will capture this event, and shut down the fragment stack.

**Persistence Layer**

Hawk library + lombok were used.  
This layer is decoupled from the whole project classes, where the implementing  
library can be easily replaced by other library when needed.  
The app will reuse the previously cached token to bypass the login screen.


**Error Handling**

Error messages sent by the server are displayed as is.  
Exceptions caught are handled as well and displayed when they make sense.

**Unit Tests**

Tests are added for the 3 ViewModels we have and 2 other components.

**Perks added to this project**

To test auth token expiry handling, go to any screen beyond login, and issue
this command from a shell/terminal:

`adb shell am broadcast -p com.example.minimoneybox -a HANDLE_EVENT --ei event 1`

This will broadcast the same event that is issued by the BroadcastAuthTokenExpiryConsumer,  
thus it will be captured by the MainActivity which in turn will:

    1. Pop all the fragments in its stack.  
    2. Navigate to LoginScreem.  
    3. Display session expiry message to the user.  