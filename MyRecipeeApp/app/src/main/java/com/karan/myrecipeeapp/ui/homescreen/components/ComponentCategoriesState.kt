package com.karan.myrecipeeapp.ui.homescreen.components

import com.karan.myrecipeeapp.data.remote.dto.categories.CategoryDtoItem


data class ComponentCategoriesState(
    val isLoading: Boolean = true,
    val categories: List<CategoryDtoItem> = emptyList(),
    val error: String = "",
)
