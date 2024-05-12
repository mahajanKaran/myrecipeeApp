package com.karan.myrecipeeapp.ui.homescreen.components

import com.karan.myrecipeeapp.data.remote.dto.recipes.RecipeDtoItem


data class ComponentTopRecipesState(
    val recipes: List<RecipeDtoItem> = emptyList(),
    val error: String = "",
    val loading: Boolean = true
)
