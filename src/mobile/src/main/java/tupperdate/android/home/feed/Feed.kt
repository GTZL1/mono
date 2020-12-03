package tupperdate.android.home.feed

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import tupperdate.android.ui.layout.SwipeStack
import tupperdate.android.ui.layout.rememberSwipeStackState
import tupperdate.api.RecipeApi

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Feed(
    recipeApi: RecipeApi,
    onReturnClick: () -> Unit,
    onRecipeClick: () -> Unit,
    onRecipeDetailsClick: (RecipeApi.Recipe) -> Unit,
    modifier: Modifier = Modifier,
) {
    val recipes by remember { recipeApi.stack() }.collectAsState(emptyList())
    Scaffold(
        bodyContent = { paddingValues ->
            SwipeStack(
                recipes,
                Modifier
                    .padding(paddingValues)
                    .padding(16.dp),
                // For the moment, reject all swipes.
                swipeStackState = rememberSwipeStackState(confirmStateChange = { false })
            ) { recipe ->
                RecipeCard(
                    recipe = recipe,
                    onInfoClick = { onRecipeDetailsClick(recipe) },
                    Modifier.fillMaxSize()
                )
            }
        },
        bottomBar = {
            RecipeActions(
                onLikeClick = { /* TODO */ },
                onDislikeClick = { /* TODO */ },
                onBackClick = onReturnClick,
                onNewRecipeClick = onRecipeClick,
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
        },
        modifier = modifier,
    )
}
