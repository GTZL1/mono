package tupperdate.android.editRecipe

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LifecycleOwnerAmbient
import androidx.lifecycle.lifecycleScope
import dev.chrisbanes.accompanist.coil.CoilImageConstants
import kotlinx.coroutines.launch
import tupperdate.api.ImageApi
import tupperdate.api.RecipeApi

@Composable
fun NewRecipe(
    recipeApi: RecipeApi,
    imageApi: ImageApi,
    onSaved: () -> Unit,
    onCancelled: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val scope = LifecycleOwnerAmbient.current.lifecycleScope

    val placeholder = "https://via.placeholder.com/450"
    val imageUri = remember { imageApi.read() }.collectAsState(initial = null).value

    val heroImage = if (imageUri == null) {
        // TODO: Remove this, we do not want to invalidate the cache just because we added an image...
        //       or maybe we do
        CoilImageConstants.defaultImageLoader().memoryCache.clear()
        placeholder
    } else {
        imageUri
    }

    val (recipe, setRecipe) = remember {
        mutableStateOf(
            EditableRecipe(
                title = "",
                description = "",
                vegan = true,
                hot = false,
                hasAllergens = true,
            )
        )
    }

    EditRecipe(
        heroImage = heroImage,
        recipe = recipe,
        onRecipeChange = setRecipe,
        onDeleteClick = { onCancelled() },
        // TODO : Actually put more data in the API.
        onSaveClick = {
            scope.launch {
                recipeApi.create(title = recipe.title, description = recipe.description)
                onSaved()
            }
        },
        onEdit = { imageApi.launch() },
        modifier = modifier,
    )
}
