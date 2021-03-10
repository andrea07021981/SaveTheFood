package com.example.savethefood.recipe

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.*
import com.example.savethefood.Event
import com.example.savethefood.constants.*
import com.example.savethefood.constants.ApiCallStatus.*
import com.example.savethefood.data.Result
import com.example.savethefood.data.domain.RecipeResult
import com.example.savethefood.data.source.repository.RecipeRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import kotlin.Error


class RecipeViewModel(
    private val recipeRepository: RecipeRepository,
    foodName: String?
) : ViewModel() {

    // The internal MutableLiveData that stores the status of the most recent request
    private val _status = MutableLiveData<ApiCallStatus>(Done("Done"))

    // The external immutable LiveData for the request status
    val status: LiveData<ApiCallStatus>
        get() = _status

    //livedata filter, every time it changes and emit signal the switch map is activated and filter the private list
    private val searchFilter= MutableLiveData<String>("")

    private val _reload = MutableLiveData<Boolean>(true)
    val reload: LiveData<Boolean>
        get() = _reload

    // We could also use liveData { ....onCollect() { emit(value)}}
    // TODO move all operations in repository, here only aslivedata
    // TODO move to stateflow like https://github.com/Mori-Atsushi/android-flow-mvvm-sample
    // https://github.com/Mori-Atsushi/android-flow-mvvm-sample/blob/master/app/src/main/kotlin/com/example/flow_mvvm_sample/ui/detail/DetailViewModel.kt
    var _recipeListResult: LiveData<List<RecipeResult>?> =
        recipeRepository.getRecipes(foodName)
            .onStart {
                _status.value = Loading("Loading")
            }
            .catch {
                _status.value = ApiCallStatus.Error("Error Loading") //TODO create a class with Errors
            }
            .transform { value ->
                if (value is Result.Success) {
                    emit(value.data.results)
                }
            }
            .onCompletion {
                _status.value = Done("Done")
            }
            .asLiveData(viewModelScope.coroutineContext)
     @VisibleForTesting set // this allow us to use this set only for test

    // This is the observable property, it changes every time the month filter changes (the original values never changes)
    val recipeListResult = Transformations.distinctUntilChanged(
        Transformations.map(searchFilter) { // OR    _searchFilter.switchMap { used to refresh/trigger changed livedata
            if (it != null && it.isNotEmpty()) {
                _recipeListResult.map { list ->
                    list?.filter { recipe ->
                        recipe.title.toLowerCase(Locale.getDefault())
                            .contains(it.toLowerCase(Locale.getDefault()))
                    }
                }
            } else {
                _recipeListResult
            }
        }
    )

    private val _recipeDetailEvent = MutableLiveData<Event<RecipeResult>>()
    val recipeDetailEvent: LiveData<Event<RecipeResult>>
        get() = _recipeDetailEvent

    fun moveToRecipeDetail(recipe: RecipeResult) {
        _recipeDetailEvent.value = Event(recipe)
    }

    fun updateDataList(filter: String) {
       searchFilter.value = filter
    }

    fun reloadList() {
        // TODO Reload using a variable and _reload.switchMap {..} search online android flow reload data
    }

    class RecipeViewModelFactory(
        private val dataRepository: RecipeRepository,
        private val foodName: String?
    ) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(RecipeViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return RecipeViewModel(
                    dataRepository,
                    foodName
                ) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }

    }
}