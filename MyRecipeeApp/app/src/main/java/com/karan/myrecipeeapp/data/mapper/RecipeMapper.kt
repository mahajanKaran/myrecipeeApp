package com.karan.myrecipeeapp.data.mapper

import com.karan.myrecipeeapp.data.local.LocalRecipeCategoryEntity
import com.karan.myrecipeeapp.data.local.LocalRecipeEntity
import com.karan.myrecipeeapp.data.local.RecipeEntity
import com.karan.myrecipeeapp.data.remote.dto.categories.CategoryDtoItem
import com.karan.myrecipeeapp.data.remote.dto.recipes.RecipeDtoItem
import com.karan.myrecipeeapp.domain.model.ModelLocalRecipe

fun RecipeEntity.toRecipeDtoItem(): RecipeDtoItem = RecipeDtoItem(
    imageUrl = imageUrl,
    ingredient = ingredient,
    method = method,
    tag = tag,
    title = title,
)

fun RecipeDtoItem.toRecipeEntity(): RecipeEntity = RecipeEntity(
    imageUrl = imageUrl,
    ingredient = ingredient,
    method = method,
    tag = tag,
    title = title
)

fun RecipeDtoItem.toLocalRecipeEntity(): LocalRecipeEntity = LocalRecipeEntity(
    imageUrl = imageUrl,
    ingredient = ingredient,
    method = method,
    tag = tag,
    title = title
)

fun RecipeEntity.toLocalRecipeEntity(): LocalRecipeEntity = LocalRecipeEntity(
    imageUrl = imageUrl,
    ingredient = ingredient,
    method = method,
    tag = tag,
    title = title
)

fun LocalRecipeEntity.toModelLocalRecipe(): ModelLocalRecipe = ModelLocalRecipe(
    imageUrl = imageUrl,
    ingredient = ingredient,
    method = method,
    tag = tag,
    title = title
)

fun ModelLocalRecipe.toRecipeDtoItem(): RecipeDtoItem = RecipeDtoItem(
    imageUrl = imageUrl,
    ingredient = ingredient,
    method = method,
    tag = tag,
    title = title
)

fun CategoryDtoItem.toLocalRecipeCategoryEntity(): LocalRecipeCategoryEntity = LocalRecipeCategoryEntity(
    category = category,
    imageUrl = imageUrl
)

fun LocalRecipeCategoryEntity.toCategoryDtoItem(): CategoryDtoItem = CategoryDtoItem(
    category = category,
    imageUrl = imageUrl
)