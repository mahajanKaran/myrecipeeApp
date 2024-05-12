package com.karan.myrecipeeapp.domain.repository

import com.karan.myrecipeeapp.core.Resource
import com.karan.myrecipeeapp.data.remote.dto.categories.CategoryDtoItem
import com.karan.myrecipeeapp.data.remote.dto.recipes.RecipeDtoItem
import com.karan.myrecipeeapp.domain.model.ModelLocalRecipe
import kotlinx.coroutines.flow.Flow

interface RecipeRepository {
    suspend fun getRecipes(
        recipe: String,
        page: Int,
        pageSize: Int,
        fetchFromRemote: Boolean,
        ): Resource<List<RecipeDtoItem>>

    suspend fun getFirstFourRecipes(fetchFromRemote: Boolean): Resource<List<RecipeDtoItem>>

    suspend fun getRecipeByTitle(title: String, category: String): Flow<Resource<RecipeDtoItem>>

    suspend fun getCategories(): Flow<Resource<List<CategoryDtoItem>>>

    suspend fun getRecipesByCategory(
        recipe: String,
        category: String,
        page: Int,
        pageSize: Int,
        fetchFromRemote: Boolean,
        getSavedRecipes: Boolean,
        ): Resource<List<RecipeDtoItem>>

    suspend fun getSavedRecipes(): Resource<List<ModelLocalRecipe>>

    suspend fun saveRecipe(recipeDtoItem: RecipeDtoItem): Resource<String>

    suspend fun getLocalRecipeByTitle(title: String): Resource<ModelLocalRecipe?>

    suspend fun deleteSelectedSavedRecipes(recipeTitles: List<String>): String
}