package com.karan.myrecipeeapp.domain.model

import com.karan.myrecipeeapp.data.remote.dto.recipes.Ingredient


data class ModelLocalRecipe(
    val imageUrl: String = "",
    val ingredient: List<Ingredient> = listOf(),
    val method: List<String> = listOf(),
    val tag: String = "",
    val title: String = ""
)
