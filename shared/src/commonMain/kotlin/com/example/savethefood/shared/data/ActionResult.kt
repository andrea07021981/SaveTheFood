/*
 * Copyright (C) 2019 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.savethefood.shared.data

/**
 * A generic class that holds a value with its loading status.
 * @param <T>
 */
sealed class ActionResult<out R> {

    data class Success<out T>(val data: T) : ActionResult<T>()
    data class ExError(val exception: Exception) : ActionResult<Nothing>()
    data class Error(val message: String) : ActionResult<Nothing>()
    object Loading : ActionResult<Nothing>()
    object Idle : ActionResult<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is ExError -> "Exception[exception=$exception]"
            is Error -> "Error[exception=$message]"
            is Loading -> "Loading"
            Idle -> "Idle"
        }
    }
}

/**
 * `true` if [ActionResult] is of type [Success] & holds non-null [Success.data].
 */
val ActionResult<*>.succeeded
    get() = this is ActionResult.Success && data != null
