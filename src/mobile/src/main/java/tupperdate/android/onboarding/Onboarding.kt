package tupperdate.android.onboarding

import androidx.compose.foundation.Text
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.ui.tooling.preview.Preview
import tupperdate.android.R
import tupperdate.android.ui.TupperdateTheme
import tupperdate.android.ui.TupperdateTypography
import tupperdate.android.ui.material.BrandedTextField

@Composable
fun Onboarding(
    onButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val (phone, setPhone) = remember { mutableStateOf("") }

    Column(
        modifier.padding(top = 64.dp, bottom = 42.dp, start = 16.dp, end = 16.dp)
    ) {
        Text(
            text = stringResource(R.string.onboarding_welcome),
            style = TupperdateTypography.h3,
            modifier = Modifier.padding(bottom = 8.dp),
        )

        Text(
            text = "Discover folks who cook what you like, and start sharing meals with them today.",
            modifier = Modifier.padding(bottom = 16.dp)
        )

        BrandedTextField(
            value = phone,
            onValueChange = setPhone,
            label = { Text("Your telephone number") },
            placeholder = { Text("+41 79 123 45 67") },
            keyboardType = KeyboardType.Phone,
            modifier = Modifier
                .fillMaxWidth()
        )


        Row(modifier = Modifier.weight(1f)) {}

        BottomBar(
            buttonValue = "Get Started",
            onButtonClick = onButtonClick,
            bottomText = "This app was created during a group project at HEIG-VD. Make sure to check it out on GitHub."
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun OnboardingPreview() {
    TupperdateTheme {
        Onboarding(
            {},
            Modifier.background(Color.White)
                .fillMaxSize()
        )
    }
}