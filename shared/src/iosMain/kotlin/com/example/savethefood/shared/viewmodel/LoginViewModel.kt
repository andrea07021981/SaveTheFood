package com.example.savethefood.shared.viewmodel

import com.example.savethefood.shared.data.source.repository.UserRepository
import dev.icerock.moko.mvvm.livedata.MutableLiveData

actual class LoginViewModel actual constructor(
    val userDataRepository: UserRepository
) {

    // TODO add basic code here (Flows, etc), then inherit Swift controller viewmodel from here (it extends ObservableObject)
    val test = MutableLiveData<String>("Value from shared")
}