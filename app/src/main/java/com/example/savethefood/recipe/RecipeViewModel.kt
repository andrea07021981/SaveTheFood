package com.example.savethefood.recipe

import androidx.annotation.VisibleForTesting
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.example.savethefood.Event
import com.example.savethefood.constants.ApiCallStatus
import com.example.savethefood.constants.ApiCallStatus.Done
import com.example.savethefood.constants.ApiCallStatus.Loading
import com.example.savethefood.data.Result
import com.example.savethefood.data.domain.RecipeResult
import com.example.savethefood.data.source.repository.RecipeRepository
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.transform
import java.lang.Exception
import java.util.*


class RecipeViewModel @ViewModelInject constructor(
    private val recipeRepository: RecipeRepository,
    @Assisted private val foodName: SavedStateHandle?
) : ViewModel() {

    // The internal MutableLiveData that stores the status of the most recent request
    // TODO REmove and use the result loading ....
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
    private var _recipeListResult: LiveData<List<RecipeResult>?> =
        recipeRepository.getRecipes(foodName?.get("foodName"))
            .onStart {
                emit(Result.Loading)
            }
            .catch { error ->
                emit(Result.ExError(Exception(error.message)))
            }
            .transform { value ->
                when (value) {
                    is Result.Loading -> _status.value = Loading()
                    is Result.Success -> {
                        _status.value = Done("Done")
                        emit(value.data.results)
                    }
                    is Result.ExError -> _status.value = ApiCallStatus.Error(
                        value.exception.localizedMessage
                    )
                    else -> Unit
                }
            }
            .onCompletion {
                // In this case it's called because the channel is closed in datasource
                _status.value = Done()
            }
            .asLiveData(viewModelScope.coroutineContext)
     @VisibleForTesting set // this allow us to use this set only for test

    // This is the observable property, it changes every time the month filter changes (the original values never changes)
    val recipeListResult = Transformations.distinctUntilChanged(
        Transformations.switchMap(searchFilter) { // OR    _searchFilter.switchMap { used to refresh/trigger changed livedata
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
}