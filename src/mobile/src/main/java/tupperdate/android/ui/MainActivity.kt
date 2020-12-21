package tupperdate.android.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.platform.setContent
import tupperdate.android.data.legacy.api
import tupperdate.android.ui.theme.TupperdateTheme

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val api = api() // TODO : Get rid of this.

        injectAllTheThings() // Koin DI.

        setContent {
            TupperdateTheme {
                TupperdateApp(
                    api = api,
                )
            }
        }
    }
}
