package com.karan.myrecipeeapp.ui.categoriesscreen.components

import com.karan.myrecipeeapp.data.remote.dto.categories.CategoryDtoItem


data class CategoryListState(
    val isLoading: Boolean = false,
    val categories: List<CategoryDtoItem> = emptyList(),
    val error: String = "",
)
