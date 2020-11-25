package tupperdate.android.home

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.ui.tooling.preview.Preview
import dev.chrisbanes.accompanist.coil.CoilImage
import tupperdate.android.R
import tupperdate.android.ui.TupperdateTheme
import tupperdate.android.ui.modifier.shade
import tupperdate.api.RecipeApi


/**
 * A composable that displays a certain recipe.
 *
 * @param recipe the [RecipeApi.Recipe] data to display.
 * @param onInfoClick callback called whenever the info button is clicked.
 * @param modifier the [Modifier] for this composable.
 */
@Composable
fun RecipeCard(
    recipe: RecipeApi.Recipe,
    onInfoClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    RecipeCard(
        title = recipe.title,
        subtitle = recipe.description,
        imageUrl = recipe.pictureUrl,
        onInfoClick = onInfoClick,
        modifier = modifier,
    )
}

/**
 * A composable that displays a certain recipe, along with some information.
 *
 * @param title the title of the displayed recipe
 * @param subtitle the subtitle of the displayed recipe. Should be formatted
 * @param imageUrl the URL at which the recipe image should be fetched
 * @param onInfoClick callback called whenever the info button is clicked
 * @param modifier the [Modifier] for this composable
 */
@Composable
fun RecipeCard(
    title: String,
    subtitle: String,
    imageUrl: String,
    onInfoClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier.preferredSize(width = 308.dp, height = 362.dp),
        elevation = 4.dp,
        contentColor = Color.White,
    ) {
        Box {
            // Background that fills the card.
            CoilImage(
                data = imageUrl,
                Modifier.shade().fillMaxSize(),
                fadeIn = true,
                contentScale = ContentScale.Crop,
            )
            // Recipe information.
            Row(
                Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 32.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column {
                    Text(title, style = MaterialTheme.typography.h6)
                    Text(subtitle, style = MaterialTheme.typography.subtitle2)
                }
                Spacer(Modifier.weight(1f, true))
                IconButton(onInfoClick) {
                    Icon(vectorResource(R.drawable.ic_home_recipe_card_help))
                }
            }
        }
    }
}

// PREVIEWS

@Preview
@Composable
private fun RecipeCardPreview() {
    TupperdateTheme {
        RecipeCard(
            title = "Chilli con carne",
            subtitle = "575 Kcal.",
            imageUrl = "https://thispersondoesnotexist.com/image",
            onInfoClick = {},
            Modifier.padding(16.dp),
        )
    }
}
