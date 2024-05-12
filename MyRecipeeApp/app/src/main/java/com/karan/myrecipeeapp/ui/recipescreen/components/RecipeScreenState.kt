package com.karan.myrecipeeapp.ui.recipescreen.components

import com.karan.myrecipeeapp.data.remote.dto.recipes.RecipeDtoItem


data class RecipeScreenState(
    val recipe: RecipeDtoItem = RecipeDtoItem(),
    val isLoading: Boolean = true,
    val error: String = "",
)
