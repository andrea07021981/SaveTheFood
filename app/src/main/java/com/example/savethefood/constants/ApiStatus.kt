package com.example.savethefood.constants


@Deprecated("Moved to shared")
sealed class ApiCallStatus(
    open val message: String
) {

    // Initial state, we are loading data
    @Deprecated("Moved to shared")
    data class Loading(override val message: String = "Loading") : ApiCallStatus(message)

    // Error state
    @Deprecated("Moved to shared")
    data class Error(override val message: String = "Error") : ApiCallStatus(message)

    // We have received data
    @Deprecated("Moved to shared")
    data class Done(override val message: String = "Done") : ApiCallStatus(message)
}
