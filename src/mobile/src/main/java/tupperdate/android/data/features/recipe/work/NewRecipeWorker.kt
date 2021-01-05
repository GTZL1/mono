package tupperdate.android.data.features.recipe.work

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import io.ktor.client.*
import io.ktor.client.request.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.apache.commons.codec.binary.Base64
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import tupperdate.android.data.InternalDataApi
import tupperdate.android.data.features.recipe.NewRecipe
import tupperdate.common.dto.NewRecipeDTO
import tupperdate.common.dto.RecipeAttributesDTO
import java.io.ByteArrayOutputStream

@OptIn(KoinApiExtension::class)
@InternalDataApi
class NewRecipeWorker(
    context: Context,
    params: WorkerParameters,
) : CoroutineWorker(context, params), KoinComponent {

    private val client: HttpClient by inject()

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        val base64Image = inputData.getString(RecipeImageUri)
            ?.let(Uri::parse)
            ?.readFileAndCompressAsBase64(applicationContext.contentResolver)

        val dto = NewRecipeDTO(
            title = requireNotNull(inputData.getString(RecipeTitle)),
            description = requireNotNull(inputData.getString(RecipeDescription)),
            attributes = RecipeAttributesDTO(
                vegetarian = inputData.getBoolean(RecipeIsVegetarian, false),
                warm = inputData.getBoolean(RecipeIsWarm, false),
                hasAllergens = inputData.getBoolean(RecipeHasAllergens, false),
            ),
            imageBase64 = base64Image,
        )

        return@withContext try {
            // TODO : Compress images.
            client.post<Unit>("/recipes") {
                body = dto
            }
            Result.success()
        } catch (problem: Throwable) {
            Result.retry()
        }
    }

    companion object {

        private const val RecipeTitle = "title"
        private const val RecipeDescription = "description"
        private const val RecipeIsVegetarian = "vegetarian"
        private const val RecipeIsWarm = "warm"
        private const val RecipeHasAllergens = "allergens"
        private const val RecipeImageUri = "picture"

        /**
         * Creates the [Data] associated with the creation of a [NewRecipe].
         */
        @InternalDataApi
        fun inputData(recipe: NewRecipe): Data {
            return workDataOf(
                RecipeTitle to recipe.title,
                RecipeDescription to recipe.description,
                RecipeIsVegetarian to recipe.isVegan,
                RecipeIsWarm to recipe.isWarm,
                RecipeHasAllergens to recipe.hasAllergens,
                RecipeImageUri to recipe.picture?.toString(),
            )
        }
    }
}

// This could be adjusted depending on some device configuration. Ranges from 0-100
private const val CompressFactor = 10

// TODO : Factorize this.
private fun Uri.readFileAndCompressAsBase64(contentResolver: ContentResolver): String {
    val bitmap = BitmapFactory.decodeStream(contentResolver.openInputStream(this))
    val array = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, CompressFactor, array)
    return array.toByteArray().let(Base64::encodeBase64String)
}
