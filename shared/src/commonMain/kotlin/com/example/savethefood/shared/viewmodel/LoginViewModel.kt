package com.example.savethefood.shared.viewmodel

import com.example.savethefood.shared.data.source.repository.UserRepository

// TODO need improvements in koin before moving VMs here
expect class LoginViewModel(
    userDataRepository: UserRepository
)