package tupperdate.android.data.features.recipe

import com.dropbox.android.external.store4.Fetcher
import com.dropbox.android.external.store4.FetcherResult
import io.ktor.client.*
import io.ktor.client.request.*
import kotlinx.coroutines.flow.flowOf
import tupperdate.common.dto.RecipeDTO

object RecipeFetcher {

    private const val RecipesFetchCount = 4

    /**
     * A [Fetcher] that retrieves the most recent recipes for the currently logged in user. The
     * amount of fetched recipes should be provided as an argument.
     */
    fun allRecipesFetcher(client: HttpClient): Fetcher<Unit, List<RecipeDTO>> {
        return Fetcher.of {
            return@of client.get<List<RecipeDTO>>("/recipes") {
                parameter("count", RecipesFetchCount)
            }
        }
    }

    /**
     * A [Fetcher] that retrieves a single [Recipe] for the currently logged in user.
     */
    fun singleRecipeFetcher(client: HttpClient): Fetcher<String, RecipeDTO> {
        return Fetcher.ofResultFlow {
            // TODO : Implement this.
            flowOf(FetcherResult.Error.Message("Not implemented yet."))
        }
    }
}
