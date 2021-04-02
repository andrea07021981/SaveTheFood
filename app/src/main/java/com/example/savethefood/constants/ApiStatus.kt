package com.example.savethefood.constants

// TODO deprecate it and use result
sealed class ApiCallStatus(
    open val message: String
) {

    // Initial state, we are loading data
    data class Loading(override val message: String = "Loading") : ApiCallStatus(message)

    // Error state
    data class Error(override val message: String = "Error") : ApiCallStatus(message)

    // We have received data
    data class Done(override val message: String = "Done") : ApiCallStatus(message)
}
