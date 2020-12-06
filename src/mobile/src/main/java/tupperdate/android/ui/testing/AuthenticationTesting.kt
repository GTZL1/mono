package tupperdate.android.ui.testing

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.*
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import tupperdate.android.data.legacy.api.Api
import tupperdate.android.data.legacy.api.AuthenticationApi
import tupperdate.android.data.legacy.api.UserApi

@Composable
fun AuthenticationTesting(
    api: Api,
    modifier: Modifier = Modifier,
) {
    val scope = AmbientLifecycleOwner.current.lifecycleScope

    val (phone, setPhone) = remember { mutableStateOf("") }
    val (verification, setVerification) = remember { mutableStateOf("") }

    val (requestCodeResult, setRequestCodeResult) = remember {
        mutableStateOf<AuthenticationApi.RequestCodeResult?>(null)
    }
    val (verificationResult, setVerificationResult) = remember {
        mutableStateOf<AuthenticationApi.VerificationResult?>(null)
    }
    val profile by remember { api.users.profile }.collectAsState(null)
    val authInfo by remember { api.authentication.auth }.collectAsState(null)

    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {

        TextField(
            value = phone,
            onValueChange = setPhone,
            label = { Text("Phone") },
        )
        RequestCodeStatus(requestCodeResult)

        TextField(
            value = verification,
            onValueChange = setVerification,
            label = { Text("Verification code") },
        )
        VerificationResultStatus(verificationResult)

        Button(onClick = {
            scope.launch {
                setRequestCodeResult(null)
                setRequestCodeResult(api.authentication.requestCode(phone))
            }
        }) { Text("Send code") }

        Button(onClick = {
            scope.launch {
                setVerificationResult(null)
                setVerificationResult(api.authentication.verify(verification))
            }
        }) { Text("Verify code") }

        Profile(profile)
        JWTToken(authInfo?.token ?: "Not available")
    }
}

@Composable
private fun RequestCodeStatus(
    state: AuthenticationApi.RequestCodeResult?,
    modifier: Modifier = Modifier,
) {
    val text = when (state) {
        AuthenticationApi.RequestCodeResult.LoggedIn -> "LoggedIn"
        AuthenticationApi.RequestCodeResult.RequiresVerification -> "RequiresVerification"
        AuthenticationApi.RequestCodeResult.InternalError -> "InternalError"
        AuthenticationApi.RequestCodeResult.InvalidNumberError -> "InvalidNumberError"
        else -> "Not set"
    }
    Text("RequestCodeStatus $text", modifier)
}

@Composable
private fun VerificationResultStatus(
    state: AuthenticationApi.VerificationResult?,
    modifier: Modifier = Modifier,
) {
    val text = when (state) {
        AuthenticationApi.VerificationResult.LoggedIn -> "LoggedIn"
        AuthenticationApi.VerificationResult.InvalidVerificationError -> "InvalidVerificationError"
        AuthenticationApi.VerificationResult.InternalError -> "InternalError"
        null -> "Not set"
    }
    Text("VerificationStatus $text", modifier)
}

@Composable
private fun Profile(
    state: UserApi.Profile?,
    modifier: Modifier = Modifier,
) {
    Text("Profile $state", modifier)
}

@Composable
private fun JWTToken(
    token: String,
    modifier: Modifier = Modifier,
) {
    Column(modifier) {
        Text("JWT Token", style = MaterialTheme.typography.overline)
        Text(
            token,
            style = MaterialTheme.typography.body1,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        val clipboard = AmbientClipboardManager.current
        val context = AmbientContext.current
        Button(onClick = {
            clipboard.setText(AnnotatedString(token))
            Toast.makeText(context, "JWT token in clipboard", Toast.LENGTH_SHORT).show()
        }) {
            Text("Copy to clipboard")
        }
    }
}
