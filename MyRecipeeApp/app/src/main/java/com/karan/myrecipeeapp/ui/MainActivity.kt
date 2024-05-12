package com.karan.myrecipeeapp.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.karan.myrecipeeapp.core.Constants
import com.karan.myrecipeeapp.core.Screen
import com.karan.myrecipeeapp.ui.categoriesscreen.CategoriesScreen
import com.karan.myrecipeeapp.ui.homescreen.HomeScreen
import com.karan.myrecipeeapp.ui.recipelistscreen.RecipeListScreen
import com.karan.myrecipeeapp.ui.recipescreen.RecipeScreen
import com.karan.myrecipeeapp.ui.theme.RecipeeAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RecipeeAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = Screen.HomeScreen.route
                    ) {
                        composable(route = Screen.HomeScreen.route) {
                            HomeScreen(navController = navController) {
                                finish()
                            }
                        }
                        composable(
                            route = Screen.RecipeScreen.route + "/{${Constants.RECIPE_SCREEN_RECIPE_TITLE_KEY}}/{${Constants.RECIPE_SCREEN_RECIPE_CATEGORY_KEY}}/{${Constants.RECIPE_SCREEN_SHOULD_LOAD_FROM_SAVED_RECIPES}}",
                            arguments = listOf(
                                navArgument(name = Constants.RECIPE_SCREEN_RECIPE_TITLE_KEY) {
                                    type = NavType.StringType
                                },
                                navArgument(name = Constants.RECIPE_SCREEN_RECIPE_CATEGORY_KEY) {
                                    type = NavType.StringType
                                },
                                navArgument(name = Constants.RECIPE_SCREEN_SHOULD_LOAD_FROM_SAVED_RECIPES)
                                {
                                    type = NavType.BoolType
                                },
                            )
                        ) {
                            RecipeScreen(navController = navController)
                        }
                        composable(
                            route = Screen.RecipeListScreen.route + "/{${Constants.RECIPE_LIST_SCREEN_RECIPE_CATEGORY_KEY}}/{${Constants.RECIPE_LIST_SCREEN_RECIPE_IMAGE_URL_KEY}}/{${Constants.RECIPE_SCREEN_SHOULD_LOAD_FROM_SAVED_RECIPES}}",
                            arguments = listOf(
                                navArgument(name = Constants.RECIPE_LIST_SCREEN_RECIPE_CATEGORY_KEY) {
                                    type = NavType.StringType
                                },
                                navArgument(name = Constants.RECIPE_LIST_SCREEN_RECIPE_IMAGE_URL_KEY)
                                {
                                    type = NavType.StringType
                                },
                                navArgument(name = Constants.RECIPE_SCREEN_SHOULD_LOAD_FROM_SAVED_RECIPES)
                                {
                                    type = NavType.BoolType
                                },
                            )
                        ) {
                            RecipeListScreen(navController = navController)
                        }

                        composable(route = Screen.CategoriesScreen.route) {
                            CategoriesScreen(
                                navController = navController
                            )
                        }
                    }
                }
            }
        }
    }
}

