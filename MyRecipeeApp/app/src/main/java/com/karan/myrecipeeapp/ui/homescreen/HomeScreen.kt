package com.karan.myrecipeeapp.ui.homescreen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.More
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.SubcomposeAsyncImage
import com.karan.myrecipeeapp.R
import com.karan.myrecipeeapp.core.MyPadding
import com.karan.myrecipeeapp.core.Screen
import com.karan.myrecipeeapp.core.robotoFonts
import kotlinx.coroutines.flow.collectLatest

@Composable
fun HomeScreen(
    viewModel: HomeScreenViewModel = hiltViewModel(),
    navController: NavHostController,
    onFinishCalled: () -> Unit,
) {
    val topRecipesState by viewModel.topRecipes
    val categoriesState by viewModel.categoriesState
    val scaffoldState = rememberScaffoldState()
    LaunchedEffect(Unit) {
        viewModel.uiEvents.collectLatest {
            when (it) {
                HomeScreenUiEvents.CloseNavDrawer -> {
                    scaffoldState.drawerState.close()
                }

                HomeScreenUiEvents.OpenNavDrawer -> {
                    scaffoldState.drawerState.open()
                }

                HomeScreenUiEvents.NavigateUp -> {
                    navController.navigateUp()
                }

                HomeScreenUiEvents.NavigateToSearchRecipesScreen -> {
                    navController.navigate(
                        route = Screen.RecipeListScreen.route + "/ /${
                            "Search"
                        }/false"
                    )
                }

                HomeScreenUiEvents.NavigateToCategoriesScreen -> {
                    navController.navigate(route = Screen.CategoriesScreen.route)
                }
            }
        }
    }

    BackHandler {
        if (scaffoldState.drawerState.isOpen) {
            viewModel.sendUiEvents(HomeScreenUiEvents.CloseNavDrawer)
        } else {
            onFinishCalled()
        }
    }

    Scaffold(
        scaffoldState = scaffoldState,
        drawerContent = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colors.background)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_launcher_background),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                        .padding(MyPadding.medium),
                    contentScale = ContentScale.FillWidth,
                )

                Row(modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        navController.navigate(Screen.RecipeListScreen.route + "/saved/search/true")
                        viewModel.sendUiEvents(HomeScreenUiEvents.CloseNavDrawer)
                    }
                    .padding(MyPadding.medium), verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Favourites")
                    Spacer(modifier = Modifier.width(MyPadding.small))
                    IconButton(onClick = { navController.navigate(Screen.RecipeListScreen.route + "/saved/fav/true") }) {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = "saved recipes"
                        )
                    }
                }

                Row(modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        viewModel.sendUiEvents(HomeScreenUiEvents.NavigateToCategoriesScreen)
                        viewModel.sendUiEvents(HomeScreenUiEvents.CloseNavDrawer)
                    }
                    .padding(MyPadding.medium), verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Categories")
                    Spacer(modifier = Modifier.width(MyPadding.small))
                    IconButton(onClick = { viewModel.sendUiEvents(HomeScreenUiEvents.NavigateToCategoriesScreen) }) {
                        Icon(imageVector = Icons.Default.More, contentDescription = "saved recipes")
                    }
                }

            }
        }
    ) { padding ->
        LazyColumn() {
            item(1) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(LocalConfiguration.current.screenHeightDp.dp / 2)
                            /*.graphicsLayer {
                                shadowElevation = 8.dp.toPx()
                                shape = CustomShape() //CustomShape
                                clip = true
                            }*/
                            .drawBehind {
                                drawRect(color = Color(0xFF000000))
                            }, contentAlignment = Alignment.Center
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.TopCenter)
                                .drawBehind {
                                    drawRect(Color.Transparent)
                                },
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,

                            ) {
                            IconButton(
                                onClick = { viewModel.sendUiEvents(HomeScreenUiEvents.OpenNavDrawer) },
                                modifier = Modifier.padding(MyPadding.small)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Menu,
                                    contentDescription = "open menu",
                                    modifier = Modifier.size(30.dp)
                                )
                            }

                            IconButton(
                                onClick = { viewModel.sendUiEvents(HomeScreenUiEvents.NavigateToSearchRecipesScreen) },
                                modifier = Modifier.padding(MyPadding.small)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = "search recipes",
                                    modifier = Modifier.size(30.dp)
                                )
                            }

                        }

                        SubcomposeAsyncImage(
                            model = "",
                            loading = {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(50.dp),
                                    color = MaterialTheme.colors.primaryVariant
                                )
                            },
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxSize()
                            ,
                            contentScale = ContentScale.Crop,
                            filterQuality = FilterQuality.Medium
                        )
                        Text(
                            text = "Seems like you are hungry, let's get you some food",
                            style = MaterialTheme.typography.h4,
                            fontWeight = FontWeight.ExtraLight,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colors.onSurface,
                            fontFamily = robotoFonts
                        )
                    }
                }
            }
            item(2) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Top Recipes",
                        fontFamily = robotoFonts,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(MyPadding.medium),
                        style = MaterialTheme.typography.h5
                    )
                    IconButton(onClick = viewModel::refreshTopRecipes) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Refresh top recipes"
                        )
                    }
                }
            }
            item(3) {
                when {
                    topRecipesState.loading -> {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = MaterialTheme.colors.primaryVariant)
                        }
                    }

                    topRecipesState.error.isNotBlank() -> {
                        Text(
                            text = topRecipesState.error,
                            color = Color.Yellow,
                            modifier = Modifier.padding(horizontal = MyPadding.medium)
                        )
                    }

                    else -> {
                        LazyRow(verticalAlignment = Alignment.CenterVertically) {
                            items(topRecipesState.recipes) { item ->
                                Column(
                                    modifier = Modifier
                                        .width(250.dp)
                                        .height(170.dp)
                                        .padding(horizontal = MyPadding.medium)
                                        .clickable {
                                            navController.navigate(Screen.RecipeScreen.route + "/${item.title}/${item.tag}/false") {
                                                launchSingleTop = true
                                            }
                                        }
                                )
                                {
                                    SubcomposeAsyncImage(
                                        model = item.imageUrl,
                                        contentDescription = null,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .fillMaxHeight(0.6f)
                                            .graphicsLayer {
                                                shape = RoundedCornerShape(MyPadding.medium)
                                                clip = true
                                            },
                                        contentScale = ContentScale.Crop,
                                        loading = {
                                            CircularProgressIndicator(
                                                modifier = Modifier.size(20.dp),
                                                color = MaterialTheme.colors.primaryVariant
                                            )
                                        },
                                        filterQuality = FilterQuality.Medium,
                                    )
                                    Spacer(modifier = Modifier.width(MyPadding.small))
                                    Text(
                                        text = item.title,
                                        fontFamily = robotoFonts,
                                        fontWeight = FontWeight.Normal,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .fillMaxHeight()
                                    )
                                    Spacer(modifier = Modifier.width(MyPadding.small))
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(MyPadding.medium))
                    }
                }
            }

            item(4) {

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = MyPadding.small)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Categories",
                            fontFamily = robotoFonts,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(MyPadding.medium),
                            style = MaterialTheme.typography.h5
                        )
                        IconButton(onClick = viewModel::refreshCategories) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = "Refresh categories"
                            )
                        }
                    }

                    Text(
                        text = "View All",
                        modifier = Modifier
                            .padding(MyPadding.medium)
                            .clickable {
                                viewModel.sendUiEvents(HomeScreenUiEvents.NavigateToCategoriesScreen)
                            },
                        fontFamily = robotoFonts,
                        fontWeight = FontWeight.Medium,
                        style = MaterialTheme.typography.h6,
                    )
                }
            }

            item(5) {
                when {
                    categoriesState.isLoading -> {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = MaterialTheme.colors.primaryVariant)
                        }
                    }
                    categoriesState.error.isNotBlank() -> {
                        Text(
                            text = categoriesState.error,
                            color = Color.Yellow,
                            modifier = Modifier.padding(horizontal = MyPadding.medium)
                        )
                    }
                    else -> {
                        LazyRow(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            items(categoriesState.categories) { item ->
                                //                        val encodedUrl = URLEncoder.encode("http://alphaone.me/", StandardCharsets.UTF_8.toString())
                                Column(
                                    modifier = Modifier
                                        .width(250.dp)
                                        .height(170.dp)
                                        .padding(horizontal = MyPadding.medium)
                                        .clickable {
                                            navController.navigate(
                                                route = Screen.RecipeListScreen.route + "/${item.category}/${
                                                    "items"
                                                }/false"
                                            ) { launchSingleTop = true }
                                        }
                                )
                                {
                                    SubcomposeAsyncImage(
                                        model = item.imageUrl,
                                        contentDescription = null,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .fillMaxHeight(0.6f)
                                            .graphicsLayer {
                                                shape = RoundedCornerShape(MyPadding.medium)
                                                clip = true
                                            },
                                        contentScale = ContentScale.Crop,
                                        loading = {
                                            CircularProgressIndicator(
                                                modifier = Modifier.size(20.dp),
                                                color = MaterialTheme.colors.primaryVariant
                                            )
                                        },
                                        filterQuality = FilterQuality.Medium,
                                    )
                                    Spacer(modifier = Modifier.width(MyPadding.small))
                                    Text(
                                        text = item.category,
                                        fontFamily = robotoFonts,
                                        fontWeight = FontWeight.Normal,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .fillMaxHeight()
                                    )
                                    Spacer(modifier = Modifier.width(MyPadding.small))
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(MyPadding.medium))
                    }
                }
            }
        }
    }
}