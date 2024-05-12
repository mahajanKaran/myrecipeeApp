package com.karan.myrecipeeapp.data.remote.dto.recipes

import com.karan.myrecipeeapp.data.remote.dto.recipes.Ingredient

data class RecipeDtoItem(
    val imageUrl: String = "",
    val ingredient: List<Ingredient> = listOf(),
    val method: List<String> = listOf(),
    val tag: String = "",
    val title: String = ""
)