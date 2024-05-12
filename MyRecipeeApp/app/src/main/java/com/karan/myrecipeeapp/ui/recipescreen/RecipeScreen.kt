package com.karan.myrecipeeapp.ui.recipescreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.SubcomposeAsyncImage
import com.karan.myrecipeeapp.core.MyPadding
import com.karan.myrecipeeapp.core.robotoFonts
import kotlinx.coroutines.flow.collectLatest

@Composable
fun RecipeScreen(
    navController: NavHostController,
    viewModel: RecipeScreenViewModel = hiltViewModel(),
) {
    val screenState = viewModel.recipeState.value
    val scaffoldState = rememberScaffoldState()
    val numberOfPersons = viewModel.numberOfPersons.value
    val ingredients = screenState.recipe.ingredient
    val favoriteButtonState = viewModel.favouriteState.value
    val context = LocalContext.current

    LaunchedEffect(key1 = Unit) {
        viewModel.uiRecipeScreenEvents.collectLatest { event ->
            when (event) {
                is RecipeScreenEvents.ShowSnackbar -> {
                    scaffoldState.snackbarHostState.showSnackbar(message = event.message)
                }
            }
        }
    }

    when {
        screenState.isLoading -> {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = MaterialTheme.colors.primaryVariant)
            }
        }
        screenState.error.isNotBlank() -> {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = screenState.error, color = MaterialTheme.colors.secondary)
            }
        }
        else -> {
            Scaffold(scaffoldState = scaffoldState) {padding->
                LazyColumn(modifier = Modifier.fillMaxSize()) {

                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(LocalConfiguration.current.screenHeightDp.dp / 2)
                                .drawBehind {
                                    drawRect(color = Color(0xFF000000))
                                },
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .align(Alignment.TopCenter)
                                    .drawBehind { drawRect(color = Color.Transparent) },
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                IconButton(
                                    onClick = { navController.navigateUp() },
                                    modifier = Modifier.padding(4.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.ArrowBack,
                                        contentDescription = "home screen",
                                    )
                                }

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(4.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {

                                    IconButton(
                                        onClick = viewModel::onSaveRecipeButtonClicked,
                                    ) {
                                        Icon(
                                            imageVector =
                                            when (favoriteButtonState) {
                                                RecipeSaveState.NOT_SAVED -> {
                                                    Icons.Outlined.Favorite
                                                }
                                                RecipeSaveState.ALREADY_EXISTS -> {
                                                    Icons.Default.Favorite
                                                }
                                                RecipeSaveState.SAVED -> {
                                                    Icons.Default.Favorite
                                                }
                                                else -> {
                                                    Icons.Outlined.Favorite
                                                }
                                            },
                                            contentDescription = "Save Recipe",
                                            tint = if (favoriteButtonState == RecipeSaveState.NOT_SAVED) Color.White else Color.Red
                                        )
                                    }
                                }
                            }

                            SubcomposeAsyncImage(
                                model = screenState.recipe.imageUrl,
                                loading = {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(50.dp),
                                        color = MaterialTheme.colors.primaryVariant
                                    )
                                },
                                contentDescription = null,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .graphicsLayer {
                                        this.alpha = 0.25f
                                        shadowElevation = 8.dp.toPx()
                                        clip = true
                                    }
                                    .align(Alignment.Center),
                                contentScale = ContentScale.Crop,
                                filterQuality = FilterQuality.Medium
                            )
                            Text(
                                text = screenState.recipe.title,
                                style = MaterialTheme.typography.h4,
                                fontWeight = FontWeight.ExtraLight,
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colors.onSurface,
                                fontFamily = robotoFonts,
                                modifier = Modifier.align(Alignment.Center)
                            )
                        }
                    }

                    item {
                        Text(
                            text = "Ingredients",
                            fontFamily = robotoFonts,
                            fontWeight = FontWeight.Medium,
                            style = MaterialTheme.typography.h4,
                            modifier = Modifier.padding(horizontal = MyPadding.medium)
                        )
                        Spacer(modifier = Modifier.height(MyPadding.medium))
                    }


                    items(ingredients) { ingredient ->
                        val ingredientQuantity = ingredient.quantity.toFloatOrNull()
                            ?.times(viewModel.numberOfPersons.value)
                        val modifiedIngredient = if (ingredientQuantity == null) {
                            ""
                        } else {
                            "$ingredientQuantity "
                        }
                        Text(
                            text = " ${modifiedIngredient}${ingredient.description}",
                            fontFamily = robotoFonts,
                            fontWeight = FontWeight.Normal,
                            style = MaterialTheme.typography.body1,
                            modifier = Modifier.padding(horizontal = MyPadding.medium)
                        )
                        Spacer(modifier = Modifier.height(MyPadding.medium))
                    }

                    item {
                        Text(
                            text = "Method",
                            fontFamily = robotoFonts,
                            fontWeight = FontWeight.Medium,
                            style = MaterialTheme.typography.h4,
                            modifier = Modifier.padding(horizontal = MyPadding.medium)
                        )
                        Spacer(modifier = Modifier.height(MyPadding.medium))
                    }

                    items(screenState.recipe.method) { method ->
                        Text(
                            text = method,
                            fontFamily = robotoFonts,
                            fontWeight = FontWeight.Normal,
                            style = MaterialTheme.typography.body1,
                            modifier = Modifier.padding(horizontal = MyPadding.medium)
                        )
                        Spacer(modifier = Modifier.height(MyPadding.medium))
                    }
                }
            }
        }
    }
}
