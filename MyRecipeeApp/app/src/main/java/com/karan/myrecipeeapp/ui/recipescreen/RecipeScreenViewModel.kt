package com.karan.myrecipeeapp.ui.recipescreen

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.karan.myrecipeeapp.core.Constants
import com.karan.myrecipeeapp.core.Resource
import com.karan.myrecipeeapp.data.mapper.toRecipeDtoItem
import com.karan.myrecipeeapp.data.remote.dto.recipes.RecipeDtoItem
import com.karan.myrecipeeapp.domain.repository.RecipeRepository
import com.karan.myrecipeeapp.domain.usecases.UseCaseGetRecipeSavedStatus
import com.karan.myrecipeeapp.domain.usecases.UseCaseSaveRecipe
import com.karan.myrecipeeapp.ui.recipescreen.components.RecipeScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.net.URLDecoder
import java.nio.charset.StandardCharsets
import javax.inject.Inject
import kotlin.math.ceil

@HiltViewModel
class RecipeScreenViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val recipeRepository: RecipeRepository,
    private val useCaseSaveRecipe: UseCaseSaveRecipe,
    private val useCaseGetRecipeSavedStatus: UseCaseGetRecipeSavedStatus
) : ViewModel() {
    private val _recipeState = mutableStateOf<RecipeScreenState>(RecipeScreenState())
    val recipeState: State<RecipeScreenState> = _recipeState

    private val recipeTitle = mutableStateOf("")
    private val recipeCategory = mutableStateOf("")

    private val _uiRecipeScreenEvents: Channel<RecipeScreenEvents> = Channel()
    val uiRecipeScreenEvents = _uiRecipeScreenEvents.receiveAsFlow()

    private val numberOfPersonsState = mutableStateOf<Int>(1)
    val numberOfPersons = numberOfPersonsState as State<Int>

    private val _favouriteState = mutableStateOf(RecipeSaveState.UNABLE_TO_SAVE)
    val favouriteState: State<RecipeSaveState> = _favouriteState

    init {
        val recipe = savedStateHandle.get<String>(Constants.RECIPE_SCREEN_RECIPE_TITLE_KEY)
        val decodedTitle = URLDecoder.decode(recipe, StandardCharsets.UTF_8.toString())
        val category = savedStateHandle.get<String>(Constants.RECIPE_SCREEN_RECIPE_CATEGORY_KEY)
        val decodedCategory = URLDecoder.decode(category, StandardCharsets.UTF_8.toString())
        viewModelScope.launch {
            recipeTitle.value = decodedTitle ?: ""

            recipeCategory.value = decodedCategory ?: ""
            Log.d("recipescreenviewmodel", "recipe is $recipe")

            val shouldLoadFromSavedRecipes = savedStateHandle.get<Boolean>(Constants.RECIPE_SCREEN_SHOULD_LOAD_FROM_SAVED_RECIPES) ?: true
            Log.d("recipescreenviewmodel", "should load from saved recipes is $shouldLoadFromSavedRecipes")
            getRecipe(shouldLoadFromSavedRecipes = shouldLoadFromSavedRecipes)
            setFavouriteState()
        }
    }

    private suspend fun getRecipe(shouldLoadFromSavedRecipes: Boolean) {
        if (shouldLoadFromSavedRecipes) {
            val recipeResult = recipeRepository.getLocalRecipeByTitle(title = recipeTitle.value)
            when (recipeResult) {
                is Resource.Error -> {
                    _recipeState.value = _recipeState.value.copy(
                        isLoading = false,
                        error = "Unable to load recipe. Please try again later"
                    )
                }
                is Resource.Loading -> {
                    _recipeState.value = _recipeState.value.copy(isLoading = true, error = "")
                }
                is Resource.Success -> {
                    _recipeState.value = _recipeState.value.copy(
                        isLoading = false,
                        recipe = recipeResult.data?.toRecipeDtoItem() ?: RecipeDtoItem()
                    )
                }
            }
        } else {
            recipeRepository.getRecipeByTitle(
                title = recipeTitle.value,
                category = recipeCategory.value
            ).collectLatest { recipeResult ->
                when (recipeResult) {
                    is Resource.Error -> {
                        _recipeState.value = _recipeState.value.copy(
                            isLoading = false,
                            error = "Unable to load recipe. Please try again later"
                        )
                    }
                    is Resource.Loading -> {
                        _recipeState.value = _recipeState.value.copy(isLoading = true, error = "")
                    }
                    is Resource.Success -> {
                        _recipeState.value = _recipeState.value.copy(
                            isLoading = false,
                            recipe = recipeResult.data ?: RecipeDtoItem()
                        )
                    }
                }
            }
        }
    }

    fun sendRecipeScreenUiEvent(uiEvents: RecipeScreenEvents) {
        viewModelScope.launch {
            when (uiEvents) {
                is RecipeScreenEvents.ShowSnackbar -> _uiRecipeScreenEvents.send(
                    RecipeScreenEvents.ShowSnackbar(
                        message = uiEvents.message
                    )
                )
            }
        }
    }

    private fun setFavouriteState(){
        viewModelScope.launch {
            val currentRecipe = _recipeState.value.recipe
            useCaseGetRecipeSavedStatus(title = currentRecipe.title).collectLatest {
                _favouriteState.value = it
            }
            Log.d("recipescreenviewmodel","favourite state is ${recipeTitle.value}")
        }
    }

    fun onSaveRecipeButtonClicked() {
        viewModelScope.launch {
            val currentRecipe = _recipeState.value.recipe
            useCaseSaveRecipe(recipeDtoItem = currentRecipe).collectLatest {
                _favouriteState.value = it
            }
            Log.d("recipescreenviewmodel","favourite state is ${_favouriteState.value.name}")
            when (_favouriteState.value){
                RecipeSaveState.SAVED -> {
                    sendRecipeScreenUiEvent(RecipeScreenEvents.ShowSnackbar("Recipe SAVED successfully"))
                }
                RecipeSaveState.ALREADY_EXISTS -> {
                    sendRecipeScreenUiEvent(RecipeScreenEvents.ShowSnackbar("Recipe ALREADY exists"))
                }
                RecipeSaveState.UNABLE_TO_SAVE -> {
                    sendRecipeScreenUiEvent(RecipeScreenEvents.ShowSnackbar("UNABLE to save recipe, try again"))
                }
                RecipeSaveState.NOT_SAVED -> {
                    sendRecipeScreenUiEvent(RecipeScreenEvents.ShowSnackbar("REMOVED from favourites"))
                }
            }

        }
    }
}

sealed interface RecipeScreenEvents {
    class ShowSnackbar(val message: String) : RecipeScreenEvents
}