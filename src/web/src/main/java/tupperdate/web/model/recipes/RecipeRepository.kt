package tupperdate.web.model.recipes

import tupperdate.web.facade.PictureUrl
import tupperdate.web.model.Result
import tupperdate.web.model.profiles.User

interface RecipeRepository {

    suspend fun save(
        user: User,
        recipe: ModelNewRecipe,
    ): Result<Unit>

    suspend fun readOwn(
        user: User,
    ): Result<List<ModelRecipe<PictureUrl>>>

    suspend fun readAll(
        user: User,
        count: Int,
    ): Result<List<ModelRecipe<PictureUrl>>>

    suspend fun readOne(
        user: User,
        recipeId: String,
    ): Result<ModelRecipe<PictureUrl>>

    suspend fun like(
        user: User,
        recipeId: String,
    ): Result<Unit>

    suspend fun dislike(
        user: User,
        recipeId: String,
    ): Result<Unit>
}
