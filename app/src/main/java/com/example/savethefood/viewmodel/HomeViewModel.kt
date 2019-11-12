package com.example.savethefood.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.savethefood.local.database.SaveTheFoodDatabase
import com.example.savethefood.local.domain.Food
import com.example.savethefood.local.domain.User
import com.example.savethefood.repository.FoodRepository
import kotlinx.coroutines.*

class HomeViewModel(
    application: Application,
    loggedUser: User
) : AndroidViewModel(application) {

    private val viewModelJob = SupervisorJob()

    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val database = SaveTheFoodDatabase.getInstance(application)
    private val foodsRepository = FoodRepository(database)

    private var _foodList = MediatorLiveData<List<Food>>()
    val foodList: LiveData<List<Food>>
        get() = _foodList

    private var _navigateToFoodDetail = MediatorLiveData<Food>()
    val navigateToFoodDetail: LiveData<Food>
        get() = _navigateToFoodDetail


    init {
        viewModelScope.launch {
            _foodList.addSource(foodsRepository.getFoods(), _foodList::setValue)
        }
    }

    fun moveToFoodDetail(food: Food) {
        _navigateToFoodDetail.value = food
    }

    /**
     * After the navigation has taken place, make sure navigateToSelectedProperty is set to null
     */
    fun doneToFoodDetail() {
        _navigateToFoodDetail.value = null
    }

    //TODO open a barcode search fo adding a new food
    fun onAddFood() {
        viewModelScope
        Food().apply {
            foodName = "Test"
            foodImgUrl = "https://spoonacular.com/menuItemImages/cheeseburger.jpg" }
            .also {
                viewModelScope.launch {
                    foodsRepository.saveNewFood(it)
                    foodsRepository.getFoods()
                }
            }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }
    /*
     * Factory for constructing DevByteViewModel with parameter
     */
    class Factory(val app: Application, val loggedUser: User) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return HomeViewModel(app, loggedUser) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }

    }
}