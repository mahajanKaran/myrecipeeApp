package com.karan.myrecipeeapp.domain.usecases

import com.karan.myrecipeeapp.core.Resource
import com.karan.myrecipeeapp.data.remote.dto.recipes.RecipeDtoItem
import com.karan.myrecipeeapp.domain.repository.RecipeRepository
import com.karan.myrecipeeapp.ui.recipescreen.RecipeSaveState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UseCaseSaveRecipe @Inject constructor (
    private val recipeRepository: RecipeRepository,
    ) {
    suspend operator fun invoke(recipeDtoItem: RecipeDtoItem): Flow<RecipeSaveState> = flow  {
        val recipeFromDatabase = recipeRepository.getLocalRecipeByTitle(title = recipeDtoItem.title)
        when(recipeFromDatabase){
            is Resource.Error -> {
                emit(RecipeSaveState.UNABLE_TO_SAVE)
            }
            else -> {
                val recipe = recipeFromDatabase.data
                if(recipe == null){
                    recipeRepository.saveRecipe(recipeDtoItem = recipeDtoItem)
                    emit(RecipeSaveState.SAVED)
                }else{
                    recipeRepository.deleteSelectedSavedRecipes(listOf(recipeDtoItem.title))
                    emit(RecipeSaveState.NOT_SAVED)
                }
            }
        }
    }
}