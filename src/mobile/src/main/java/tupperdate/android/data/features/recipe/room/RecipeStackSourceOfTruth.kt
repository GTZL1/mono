package tupperdate.android.data.features.recipe.room

import com.dropbox.android.external.store4.SourceOfTruth
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import tupperdate.android.data.InternalDataApi
import tupperdate.android.data.features.auth.AuthenticationRepository
import tupperdate.android.data.features.recipe.Recipe
import tupperdate.common.dto.RecipeDTO

/**
 * An implementation of a [SourceOfTruth] that can manage groups of recipes (in the recipe stack)
 * and does not require a specific key to be fetched.
 *
 * @param dao the [RecipeDao] that is accessed by this [SourceOfTruth].
 */
@InternalDataApi
class RecipeStackSourceOfTruth(
    private val auth: AuthenticationRepository,
    private val dao: RecipeDao,
) : SourceOfTruth<Unit, List<RecipeDTO>, List<Recipe>> {

    override suspend fun delete(key: Unit) {
        // Ignore.
    }

    override suspend fun deleteAll() {
        // Ignore.
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun reader(key: Unit): Flow<List<Recipe>?> {
        return auth.identifier
            .flatMapLatest { dao.recipesStack(it.identifier) }
            .map { it.map(RecipeEntity::toRecipe) }
    }

    override suspend fun write(key: Unit, value: List<RecipeDTO>) {
        dao.recipesInsertAll(value.map { it.asRecipeEntity(inStack = true) })
    }
}
