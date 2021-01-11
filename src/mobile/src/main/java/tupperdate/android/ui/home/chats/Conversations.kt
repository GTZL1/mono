package tupperdate.android.ui.home.chats

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import tupperdate.android.R
import tupperdate.android.data.features.recipe.Recipe
import tupperdate.android.ui.theme.TupperdateTheme
import tupperdate.android.ui.theme.components.ProfilePicture

@Composable
fun Conversations(
    onRecipeClick: () -> Unit,
    onProfileClick: () -> Unit,
    recipes: List<Recipe>,
    conversations: List<Conversation>,
    modifier: Modifier = Modifier,
) {
    LazyColumn(modifier) {
        item {
            Header(stringResource(id = R.string.chat_matches_list))
        }
        item {
            LazyRow(
                contentPadding = PaddingValues(start = 8.dp, end = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(recipes) {
                    RecipeImage(imageUrl = it.picture)
                }
            }
        }
        item {
            Header(stringResource(R.string.chat_conversations))
        }
        items(conversations) {
            Conversation(
                title = it.title,
                subtitle = it.subtitle,
                highlighted = it.highlighted,
                image = it.image,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = {})
                    .padding(8.dp),
            )
        }
    }
}

@Composable
private fun Header(
    title: String,
    modifier: Modifier = Modifier,
) {
    Text(title, modifier.padding(16.dp), style = MaterialTheme.typography.overline)
}

@Composable
private fun RecipeImage(
    imageUrl: String?,
    modifier: Modifier = Modifier,
) {
    ProfilePicture(
        modifier = modifier.size(56.dp),
        image = imageUrl ?: "", // TODO: Fix this default value for missing images
        highlighted = false
    )
}

@Preview
@Composable
fun ConversationsPagePreview() {
    val recipes = listOf(
        Recipe(
            title = "Lobster",
            description = "From Santa Monica",
            picture = "https://www.theflavorbender.com/wp-content/uploads/2019/01/How-to-cook-Lobster-6128-700x1049.jpg",
            identifier = "id",
            timestamp = System.currentTimeMillis(),
        ),
        Recipe(
            title = "Lobster",
            description = "From Santa Monica",
            picture = "https://www.theflavorbender.com/wp-content/uploads/2019/01/How-to-cook-Lobster-6128-700x1049.jpg",
            identifier = "id",
            timestamp = System.currentTimeMillis(),
        ),
        Recipe(
            title = "Lobster",
            description = "From Santa Monica",
            picture = "https://www.theflavorbender.com/wp-content/uploads/2019/01/How-to-cook-Lobster-6128-700x1049.jpg",
            identifier = "id",
            timestamp = System.currentTimeMillis(),
        ),
    )
    val conv = listOf(
        Conversation(
            id = "12e",
            title = "Aloy",
            subtitle = "Sorry, not today !",
            highlighted = true,
            image = "https://pbs.twimg.com/profile_images/1257192502916001794/f1RW6Ogf_400x400.jpg"
        ),
        Conversation(
            id = "12d",
            title = "Ciri",
            subtitle = "Know when fairy tales cease to be tales ? When people start believing in them.",
            highlighted = false,
            image = "https://static.wikia.nocookie.net/sorceleur/images/8/81/Ciri.png/revision/latest?cb=20140902222834",
        ),
        Conversation(
            id = "12f",
            title = "The Mandalorian",
            subtitle = "This is the way.",
            highlighted = true,
            image = "https://imgix.bustle.com/uploads/image/2020/11/19/d66dd1be-49fc-46f6-b9fd-a40f20304b74-102419_disney-the-mandalorian-00-1-780x440-1572307750.jpg"
        ),
    )
    TupperdateTheme {
        Conversations(
            onRecipeClick = {},
            onProfileClick = {},
            recipes = recipes,
            conversations = conv
        )
    }
}
