package com.example.savethefood.recipe

import androidx.annotation.VisibleForTesting
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.example.savethefood.shared.data.domain.RecipeResult
import com.example.savethefood.shared.data.source.repository.RecipeRepository
import com.example.savethefood.shared.utils.ApiCallStatus
import com.example.savethefood.shared.utils.ApiCallStatus.*
import com.example.savethefood.shared.utils.Event
import com.example.savethefood.shared.utils.RecipeType
import kotlinx.coroutines.flow.*
import java.util.*

// TODO use paging library, too many recipes
@Deprecated("Moved to shared")
class RecipeViewModel @ViewModelInject constructor(
    private val recipeDataRepository: RecipeRepository,
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

    private val _recipeDetailEvent = MutableLiveData<Event<RecipeResult>>()
    val recipeDetailEvent: LiveData<Event<RecipeResult>>
        get() = _recipeDetailEvent

    // We could also use liveData { ....onCollect() { emit(value)}}
    private var _recipeListResult: LiveData<List<RecipeResult>?> =
        recipeDataRepository.getRecipes()
            .onStart {
                emit(com.example.savethefood.shared.data.Result.Loading)
            }
            .catch { error ->
                emit(com.example.savethefood.shared.data.Result.ExError(Exception(error.message)))
            }
            .transform { value ->
                when (value) {
                    is com.example.savethefood.shared.data.Result.Loading -> _status.value = Loading()
                    is com.example.savethefood.shared.data.Result.Success -> {
                        _status.value = Done("Done")
                        emit(value.data)
                    }
                    is com.example.savethefood.shared.data.Result.ExError -> _status.value = Error(
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
            if (it.isNullOrEmpty().not()) {
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

    /**
     * Observer for the recipeType and numbers of each.
     * Creates a map for all types if the list is empty
     */
    val listByRecipeType: LiveData<Map<RecipeType, Int>?> = _recipeListResult.map { result ->
        if (result?.size != 0) {
            result?.groupingBy(::recipeStorageType)?.eachCount()
        } else {
            RecipeType.values().map {
                it to 0
            }.toMap()
        }
    }

    private fun recipeStorageType(result: RecipeResult) =
        if (result.recipeId != 0L) {
            RecipeType.LOCAL
        }else {
            RecipeType.REMOTE
        }

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