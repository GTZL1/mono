package tupperdate.android.data.features.messages.store

import com.dropbox.android.external.store4.Fetcher
import com.dropbox.android.external.store4.FetcherResult
import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.request.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import tupperdate.android.data.InternalDataApi
import tupperdate.android.data.features.auth.firebase.FirebaseUid
import tupperdate.common.dto.ConversationDTO

@InternalDataApi
class ConversationFetcher(
    private val client: HttpClient,
) : Fetcher<FirebaseUid, ConversationDTO> {

    override fun invoke(
        key: FirebaseUid,
    ) = flow {

        // Until we've reached a satisfying result...
        var keepFetching = true
        while (keepFetching) {
            try {
                // ... fetch the conversation.
                val dto = client.get<ConversationDTO>("/chats/${key}")
                emit(FetcherResult.Data(dto))
                keepFetching = false

            } catch (clientException: ClientRequestException) {
                // ... or learn that the conversation does not exist.
                keepFetching = false

            } catch (throwable: Throwable) {
                // ... or handle bad connectivity.
                throwable.printStackTrace()
                emit(FetcherResult.Error.Exception(throwable))

            }

            // Suspend or cancel if needed.
            if (keepFetching) delay(LinearBackoffMillis)
        }
    }
}

// TODO : Eventually use system connectivity.
private const val LinearBackoffMillis = 16 * 1000L // A good compromise between UX and perf.
