package tupperdate.android.ui.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import tupperdate.android.data.features.messages.MessagesRepository
import tupperdate.android.data.features.messages.PendingMatch

class LoggedInViewModel(
    private val messages: MessagesRepository,
) : ViewModel() {

    val match: Flow<PendingMatch?> = messages.pending.map { it.firstOrNull() }

    /**
     * Accepts a [PendingMatch], so that it will no longer be visible in a top-level dialog that
     * indicates that a new match has occurred.
     *
     * @param match the [PendingMatch] to accept.
     */
    fun onAccept(match: PendingMatch) {
        viewModelScope.launch {
            messages.accept(match)
        }
    }
}
