package tupperdate.android.profile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ScrollableColumn
import androidx.compose.foundation.Text
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.ui.tooling.preview.Preview
import dev.chrisbanes.accompanist.coil.CoilImage
import tupperdate.android.R
import tupperdate.android.ui.TupperdateTheme
import tupperdate.android.ui.material.BrandedButton

@Composable
fun Profile(
    onCloseClick: () -> Unit,
    onEditClick: () -> Unit,
    onSaveClick: () -> Unit,
    onSignOutClick: () -> Unit,
    onFirstNameChanged: (String) -> Unit,
    onEmailChanged: (String) -> Unit,
    userImageUrl: String,
    firstName: String,
    email: String,
    modifier: Modifier = Modifier,
) {
    ScrollableColumn(modifier.fillMaxSize().padding(16.dp)) {
        Row(
            Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(id = R.string.profile_title),
                style = MaterialTheme.typography.h6
            )
            IconButton(onClick = onCloseClick) {
                Icon(
                    asset = vectorResource(id = R.drawable.ic_home_dislike_recipe),
                    tint = Color.Black
                )
            }
        }

        Row(
            Modifier.padding(top = 40.dp, bottom = 37.dp)
                .align(Alignment.CenterHorizontally)
        ) {
            Column(horizontalAlignment = (Alignment.CenterHorizontally)) {
                CoilImage(
                    modifier = Modifier.size(96.dp)
                        .clip(CircleShape)
                        .border(2.dp, Color.Gray, CircleShape),
                    data = userImageUrl,
                    contentScale = ContentScale.Crop
                )
                Button(
                    onClick = onEditClick,
                    colors = ButtonConstants.defaultButtonColors(
                        contentColor = Color.Black,
                        backgroundColor = Color.Transparent
                    ),
                    elevation = ButtonConstants.defaultElevation(0.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.profile_editpic),
                        style = MaterialTheme.typography.overline
                    )
                }
            }
        }

        OutlinedTextField(
            value = firstName,
            onValueChange = { onFirstNameChanged() }, //impossible to callback it otherwise
            modifier = Modifier.fillMaxWidth()
                .padding(bottom = 37.dp),
            label = { Text(stringResource(id = R.string.profile_name)) },
            placeholder = { Text(stringResource(id = R.string.profile_name_placeholder)) }
        )

        OutlinedTextField(
            value = email,
            onValueChange = { onEmailChanged() },
            modifier = Modifier.fillMaxWidth()
                .padding(bottom = 32.dp),
            label = { Text(stringResource(id = R.string.profile_email)) },
            placeholder = { Text(stringResource(id = R.string.profile_email_placeholder)) }
        )

        BrandedButton(
            value = stringResource(id = R.string.profile_save),
            onClick = onSaveClick,
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = onSignOutClick,
            modifier = Modifier.fillMaxWidth()
                .padding(top = 16.dp)
                .preferredHeight(56.dp),
            shape = RoundedCornerShape(50),
            border = BorderStroke(2.dp, Color.Black),
            colors = ButtonConstants.defaultButtonColors(
                contentColor = Color.Red,
                backgroundColor = Color.White
            )
        ) {
            Text(
                text = stringResource(id = R.string.profile_sign_out),
                color = Color.Red
            )
        }
    }
}


@Preview
@Composable
private fun ProfilePreview() {
    val (name, setName)= remember { mutableStateOf("Thor") }
    val (email, setEmail)= remember { mutableStateOf("thor@asgard.god") }
    TupperdateTheme {
        Profile(
            onCloseClick = {},
            onEditClick = {},
            onSaveClick = {},
            onSignOutClick = {},
            onFirstNameChanged = setName,
            onEmailChanged = setEmail,
            firstName = name,
            email = email,
            userImageUrl = "https://images.firstpost.com/wp-content/uploads/2019/04/thor380.jpg"
        )
    }
}
