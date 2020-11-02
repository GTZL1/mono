package tupperdate.android.appbars

import androidx.compose.foundation.Icon
import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.*
import androidx.compose.material.IconButton
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.vectorResource
import tupperdate.android.R

@Composable
fun mainTopBar(
    onChatClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    TopAppBar(
        title = {
            // TODO : center properly the title and change its color
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(text = "tupper.date", modifier = Modifier.align(Alignment.CenterHorizontally))
            }
        },
        navigationIcon = {
            IconButton(onClick = onChatClick) {
                Icon(vectorResource(id = R.drawable.ic_chat_24px))
            }
        },
        actions = {
            IconButton(onClick = onProfileClick) {
                Icon(vectorResource(id = R.drawable.ic_account_circle_black_18dp))
            }
        },
        backgroundColor = Color.White
    )
}
