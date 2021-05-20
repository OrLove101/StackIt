# StackIt
Android app that leverages the [StackOverflowAPI](url) to search certain or display current questions. It also allows you to send question link to friends.
The app is composed of two screens. The first screen displays a list of questions, in which, each question is described by its title, number of anwers and questioner
profile photo. After a user selects a question from the list, a second screen appears displaying web view with details about the question.

## Overview
The app does the following:
1. Search a list of questions using the [StackOverflowAPI](url)
    * Use SearchItem to search for questions
2. Display the newest questions
    * Use RefreshItem to show the newest questions
3. Show ProgressBar before profile image and WebView is loaded
4. Add a WebView to display more information about the selected question from the list
5. Use a share intent to recommend a question to friends
    * Use ShareItem to do this

To achieve this, there are five different components in this app:
1. `StackRequester` - Responsible for executing the API requests and retrieving th JSON
2. `Stack` - Model object responsible for encapsulating the attributes for each individual question
3. `StackRecyclerAdapter` - Responsible for mapping each `Stack` to a paricular view layout
4. `StackListFragment` - Responsible for fetching data, configuring the adapter and providing a search interface also show the newest questions by default or by pressing RefreshItem
    * Attached to `MainActivity`
6. `StackPageFragment` - Responsible for providing WebView and share intent
    * Attached to `StackPageActivity`

Also there is commented version without GSON library

## Libraries
* OkHttp
* GSON
* Picasso
* CircleImageView
