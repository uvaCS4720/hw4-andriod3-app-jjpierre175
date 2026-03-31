package edu.nd.pmcburne.hello

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    // repo handles data operations
    private val repo = Repository(AppDatabase.getInstance(application).locationDao())

    // holds list of locations from the db
    private val _allLocations = MutableStateFlow<List<Location>>(emptyList())
    private val _tags = MutableStateFlow<List<String>>(emptyList()) // holds all the unique tags from the db
    val tags: StateFlow<List<String>> = _tags // tags for ui

    private val _selectedTag = MutableStateFlow("core") // stores the current tag used for filtering locations
    val selectedTag: StateFlow<String> = _selectedTag // for ui

    val filteredLocations: StateFlow<List<Location>> = // derived below
        MutableStateFlow(emptyList())

    // Better: use combine in init
    private val _filteredLocations = MutableStateFlow<List<Location>>(emptyList())
    val filtered: StateFlow<List<Location>> = _filteredLocations

    init { // runs when the viewmodel is created
        viewModelScope.launch {
            repo.syncFromApi()  // sync data from api into the local db
            _allLocations.value = repo.getAllLocations() // load all the locations from the db
            _tags.value = repo.getAllTags() // load  all available tags
            applyFilter() // apply the initial filter based on the default tag
        }
    }

    fun selectTag(tag: String) { // updates the selected tag when the user chooses a new one
        _selectedTag.value = tag // update the selected tag
        applyFilter() // recalculate the filtered locations
    }

    private fun applyFilter() { // filters locations based on the currently selected tag
        _filteredLocations.value = _allLocations.value.filter { location ->
            location.tags.split(",").map { it.trim() }.contains(_selectedTag.value)
        }
    }
}