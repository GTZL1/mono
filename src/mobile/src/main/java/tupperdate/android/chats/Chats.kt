package tupperdate.android.chats

import androidx.compose.foundation.Text
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.AmbientEmphasisLevels
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.ProvideEmphasis
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow.Ellipsis
import androidx.compose.ui.unit.center
import androidx.compose.ui.unit.dp
import androidx.ui.tooling.preview.Preview
import dev.chrisbanes.accompanist.coil.CoilImage
import tupperdate.android.ui.Smurf500
import tupperdate.android.ui.TupperdateTheme
import kotlin.math.sqrt

data class Conversation(
    val id: String,
    val title: String,
    val subtitle: String,
    val highlighted: Boolean,
    val image: Any,
)

@Composable
fun Chats(
    conversations: List<Conversation>,
    onConversationClick: (Conversation) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumnFor(conversations, modifier) {
        Conversation(
            title = it.title,
            subtitle = it.subtitle,
            highlighted = it.highlighted,
            image = it.image,
            onClick = { onConversationClick(it) }
        )
    }
}

@Composable
private fun Conversation(
    title: String,
    subtitle: String,
    highlighted: Boolean,
    image: Any,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier.clickable(onClick = onClick).padding(vertical = 8.dp),
        Arrangement.spacedBy(16.dp),
        Alignment.CenterVertically
    ) {
        ConversationIcon(image, highlighted)
        val emphasis = if (highlighted) AmbientEmphasisLevels.current.high
        else AmbientEmphasisLevels.current.medium
        ProvideEmphasis(emphasis) {
            Column {
                Text(title, maxLines = 1, style = typography.subtitle1, overflow = Ellipsis)
                ProvideEmphasis(AmbientEmphasisLevels.current.medium) {
                    Text(subtitle, maxLines = 1, style = typography.subtitle2, overflow = Ellipsis)
                }
            }
        }
    }
}

@Composable
private fun ConversationIcon(
    image: Any,
    highlighted: Boolean,
    modifier: Modifier = Modifier,
) {
    CoilImage(
        image, modifier
            .dotted(highlighted)
            .clip(CircleShape)
            .size(56.dp)
            .border(4.dp, Color.Black.copy(alpha = 0.2f), CircleShape)
    )
}

/**
 * A [Modifier] which can draw a dot if the underlying layer currently requires attention. It will
 * be drawn on a 45-degree line of the biggest inner circle that could fit the composable.
 *
 * @param visible true if the circle should be drawn, false otherwise.
 */
private fun Modifier.dotted(visible: Boolean) = this then Modifier.drawWithCache {
    val radius = size.minDimension / 2 - 2.dp.toPx()
    val offset = Offset(
        x = radius * sqrt(2f) / 2f,
        y = radius * sqrt(2f) / -2f,
    )
    onDrawWithContent {
        drawContent()
        if (visible) {
            drawCircle(Color.White, 6.dp.toPx(), size.center() + offset)
            drawCircle(Color.Smurf500, 4.dp.toPx(), size.center() + offset)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ConversationPreviewHighlighted() {
    TupperdateTheme {
        Conversation(
            title = "Mario",
            subtitle = "Hey Luigi you forgot your pipe in my garden during our last party",
            highlighted = true,
            image = "https://thispersondoesnotexist.com/image",
            onClick = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ConversationPreviewNormal() {
    TupperdateTheme {
        Conversation(
            title = "Luigi",
            subtitle = "Damn that's right sorry bro I'll pick it up today today",
            highlighted = false,
            image = "https://thispersondoesnotexist.com/image",
            onClick = {},
        )
    }
}
