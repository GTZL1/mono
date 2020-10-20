package tupperdate.android.onboarding

import androidx.compose.foundation.Text
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.ui.tooling.preview.Preview
import tupperdate.android.appbars.onlyReturnTopBar
import tupperdate.android.ui.TupperdateTheme
import tupperdate.android.ui.TupperdateTypography
import tupperdate.android.ui.material.BrandedTextField

@Composable
fun OnboardingConfirmation(
    onButtonClick: () -> Unit,
    onReturnClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val (code, setCode) = remember { mutableStateOf("") }

    onlyReturnTopBar(onReturnClick)

    Column(
        modifier.padding(top = 64.dp, bottom = 42.dp, start = 16.dp, end = 16.dp)
    ) {
        Text(
            text = "You've got mail",
            style = TupperdateTypography.h5,
            modifier = Modifier.padding(top = 32.dp, bottom = 16.dp)
        )

        Text(
            text = "You're nearly there! Please enter the registration code you've just received.",
            modifier = Modifier.padding(bottom = 16.dp)
        )

        codeInput(code = code, onCodeChanged = setCode)

        // TODO Check code with database

        Row(modifier = Modifier.weight(1f)) {}

        BottomBar(
            buttonValue = "Let's Go",
            onButtonClick = onButtonClick,
            bottomText =  "Can't find your registration code? Make sure to check your spam folder."
        )
    }
}

@Composable
private fun codeInput(
    code: String,
    onCodeChanged: ((String)) -> Unit,
    modifier: Modifier = Modifier,
) {
    BrandedTextField(
        value = code,
        label = { Text(text = "Your code") },
        onValueChange = onCodeChanged,
        placeholder = { Text("2077") },
        keyboardType = KeyboardType.Number,
        modifier = modifier.fillMaxWidth(),
    )
}

@Preview(showBackground = true)
@Composable
private fun OnboardingConfirmationPreview() {
    TupperdateTheme {
        OnboardingConfirmation(
            {}, {},
            Modifier.background(Color.White)
                .fillMaxSize()
        )
    }
}