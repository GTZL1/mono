package tupperdate.android.appbars

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import tupperdate.android.R
import tupperdate.android.ui.Smurf100
import tupperdate.android.ui.components.ProfilePicture

@Composable
fun ChatTopBar(
    onReturnClick: () -> Unit,
    imageOther: Any,
    nameOther: String,
    otherOnline: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth()
            .preferredHeight(TopBarHeight)
            .background(Color.Smurf100)
            .padding(top = 9.dp, bottom = 11.dp, start = 16.dp, end = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            modifier = Modifier.padding(end = 6.dp)
                .height(18.dp),
            onClick = onReturnClick
        ) {
            Icon(asset = vectorResource(id = R.drawable.ic_back_arrow))
        }
        ProfilePicture(
            modifier = Modifier.padding(end = 6.dp),
            image = imageOther,
            highlighted = false
        )
        Column {
            Text(
                text = nameOther,
                style = MaterialTheme.typography.subtitle1
            )
            Text(
                text = otherOnline,
                style = MaterialTheme.typography.subtitle2
            )
        }
    }
}

@Composable
fun BottomBar(
    onSendClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(modifier.fillMaxWidth().background(Color.Smurf100)) {
        Row(
            Modifier.fillMaxWidth()
                .padding(
                    horizontal = 16.dp,
                    vertical = 10.dp
                )
                .background(Color.Transparent),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                modifier = Modifier.padding(end = 10.dp)
                    .height(24.dp),
                value = "",
                onValueChange = {},
                placeholder = { stringArrayResource(id = R.string.chat_message_placeholder) }
            )
            IconButton(onClick = onSendClick) {
                Icon(asset = vectorResource(id = R.drawable.ic_chat_send))
            }
        }
    }
}

private val TopBarHeight = 66.dp
