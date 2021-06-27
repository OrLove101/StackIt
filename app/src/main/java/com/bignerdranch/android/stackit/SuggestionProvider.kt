package com.bignerdranch.android.stackit

import android.content.SearchRecentSuggestionsProvider

class SuggestionProvider: SearchRecentSuggestionsProvider() {
    init {
        setupSuggestions(AUTHORITY, MODE)
    }

    companion object {
        const val AUTHORITY = "com.bignerdranch.android.stackit.SuggestionProvider"
        const val MODE: Int = SearchRecentSuggestionsProvider.DATABASE_MODE_QUERIES
    }
}