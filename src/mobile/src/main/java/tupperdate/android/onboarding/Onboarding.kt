package tupperdate.android.onboarding

import androidx.compose.foundation.ProvideTextStyle
import androidx.compose.foundation.Text
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.AmbientEmphasisLevels
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.ProvideEmphasis
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LifecycleOwnerAmbient
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import androidx.ui.tooling.preview.Preview
import kotlinx.coroutines.launch
import tupperdate.android.R
import tupperdate.android.ui.TupperdateTheme
import tupperdate.android.ui.TupperdateTypography
import tupperdate.android.ui.material.BrandedButton
import tupperdate.android.ui.material.BrandedTitleText
import tupperdate.api.AuthenticationApi
import tupperdate.api.api

@Composable
fun Onboarding(
    auth: AuthenticationApi,
    onButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val (phone, setPhone) = remember { mutableStateOf("") }
    val scope = LifecycleOwnerAmbient.current.lifecycleScope

    val (requestCodeResult, setRequestCodeResult) = remember {
        mutableStateOf<AuthenticationApi.RequestCodeResult?>(null)
    }

    Column(
        modifier.padding(top = 72.dp, bottom = 16.dp, start = 16.dp, end = 16.dp)
    ) {
        ProvideTextStyle(TupperdateTypography.h4) {
            Text(stringResource(R.string.onboarding_welcome))
            BrandedTitleText(
                stringResource(R.string.onboarding_welcome_name),
                Modifier.padding(bottom = 16.dp)
            )
        }

        Text(
            text = stringResource(R.string.onboarding_presentation),
            modifier = Modifier.padding(bottom = 32.dp)
        )

        ViewPhoneInput(
            phone = phone,
            setPhone = setPhone,
            codeResult = requestCodeResult,
            setCodeResult = setRequestCodeResult,
        )

        Spacer(modifier = Modifier.weight(1f, true))

        BrandedButton(
            value = stringResource(R.string.onboarding_button_text),
            onClick = {
                when (requestCodeResult) {
                    AuthenticationApi.RequestCodeResult.LoggedIn -> Unit
                    AuthenticationApi.RequestCodeResult.RequiresVerification -> onButtonClick()
                    AuthenticationApi.RequestCodeResult.InvalidNumberError -> Unit
                    AuthenticationApi.RequestCodeResult.InternalError -> Unit
                    null -> scope.launch {
                        setRequestCodeResult(auth.requestCode(phone))
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
        )

        ProvideEmphasis(emphasis = AmbientEmphasisLevels.current.disabled) {
            Text(
                text = stringResource(R.string.onboarding_bottom_text),
                style = TupperdateTypography.body2,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 16.dp)
            )
        }
    }
}

@Composable
private fun ViewPhoneInput(
    phone: String,
    setPhone: (String) -> Unit,
    codeResult: AuthenticationApi.RequestCodeResult?,
    setCodeResult: (AuthenticationApi.RequestCodeResult?) -> Unit,
    modifier: Modifier = Modifier,
) {
    val isErrorValue = when (codeResult) {
        AuthenticationApi.RequestCodeResult.LoggedIn -> false
        AuthenticationApi.RequestCodeResult.RequiresVerification -> false
        AuthenticationApi.RequestCodeResult.InvalidNumberError -> true
        AuthenticationApi.RequestCodeResult.InternalError -> true
        null -> false
    }

    val errorText = when (codeResult) {
        AuthenticationApi.RequestCodeResult.LoggedIn -> null
        AuthenticationApi.RequestCodeResult.RequiresVerification -> null
        AuthenticationApi.RequestCodeResult.InvalidNumberError -> "Invalid number"
        AuthenticationApi.RequestCodeResult.InternalError -> "Internal error"
        null -> null
    }

    Column(modifier = modifier) {
        OutlinedTextField(
            value = phone,
            onValueChange = {
                setCodeResult(null)
                setPhone(it)
            },
            label = { Text(stringResource(R.string.onboarding_phone_label)) },
            placeholder = { Text(stringResource(R.string.onboarding_phone_placeholder)) },
            keyboardType = KeyboardType.Phone,
            isErrorValue = isErrorValue,
            modifier = Modifier
                .fillMaxWidth()
        )

        if (errorText != null) {
            Text(
                text = errorText,
                color = MaterialTheme.colors.error,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun OnboardingPreview() {
    // Use a real device to launch the preview.
    val owner = LifecycleOwnerAmbient.current
    val api = remember { owner.api() }

    TupperdateTheme {
        Onboarding(
            api.authentication,
            {},
            Modifier.background(Color.White)
                .fillMaxSize()
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ViewPhoneInputNormalPreview() {
    val (phone, setPhone) = remember { mutableStateOf("") }

    TupperdateTheme {
        ViewPhoneInput(
            phone = phone,
            setPhone = setPhone,
            codeResult = null,
            setCodeResult = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ViewPhoneInputNormalInvalidNumber() {
    val (phone, setPhone) = remember { mutableStateOf("") }

    TupperdateTheme {
        ViewPhoneInput(
            phone = phone,
            setPhone = setPhone,
            codeResult = AuthenticationApi.RequestCodeResult.InvalidNumberError,
            setCodeResult = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ViewPhoneInputNormalInternalError() {
    val (phone, setPhone) = remember { mutableStateOf("") }

    TupperdateTheme {
        ViewPhoneInput(
            phone = phone,
            setPhone = setPhone,
            codeResult = AuthenticationApi.RequestCodeResult.InternalError,
            setCodeResult = {},
        )
    }
}
