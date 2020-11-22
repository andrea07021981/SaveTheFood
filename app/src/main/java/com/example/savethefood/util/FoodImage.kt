package com.example.savethefood.util

import androidx.annotation.DrawableRes
import com.example.savethefood.R

enum class FoodImage(val id: String) {
    EMPTY(""),
    APPLE("ic_apple_1"),
    MEAT("ic_apple_1");

}

enum class StorageType(val type: String) {
    UNKNOWN("Unknown"),
    FRIDGE("Fridge"),
    FREEZER("Freezer"),
    DRY("Dry");
}