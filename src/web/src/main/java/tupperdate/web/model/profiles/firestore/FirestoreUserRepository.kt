package tupperdate.web.model.profiles.firestore

import com.google.cloud.firestore.Firestore
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.cloud.StorageClient
import io.ktor.http.*
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import org.apache.commons.codec.binary.Base64
import tupperdate.web.facade.PictureUrl
import tupperdate.web.legacy.util.await
import tupperdate.web.model.NotFoundException
import tupperdate.web.model.Result
import tupperdate.web.model.profiles.*
import java.util.*
import java.util.concurrent.ExecutionException
import java.util.concurrent.TimeUnit

class FirestoreUserRepository(
    private val auth: FirebaseAuth,
    private val store: Firestore,
    private val storage: StorageClient,
) : UserRepository {

    override suspend fun save(
        user: ModelNewUser,
    ): Result<Unit> {
        val doc = store.collection("users").document(user.identifier)

        val id = UUID.randomUUID().toString()
        val bytes = user.displayPicture?.let { Base64.decodeBase64(it.encoded) }
        var picture : String? = null

        if (bytes != null) {
            val fileName = "$id.jpg"
            val blob = storage.bucket().create(
                fileName,
                bytes.inputStream(),
                ContentType.Image.JPEG.contentType,
            )
            // TODO: Find alternative to timeout
            val url = blob.signUrl(365, TimeUnit.DAYS)
            picture = url.toString()
        }

        val firestoreUser = user.toFirestoreUser(
            picture = picture?.let(::PictureUrl),
        )

        try {
            doc.set(firestoreUser).await()
        } catch(exception: ExecutionException) {
            return Result.BadServer(exception)
        }

        return Result.Ok(Unit)
    }

    override suspend fun read(
        user: User,
    ): Result<ModelUser> = coroutineScope {
        val firestoreUser = async {
            store.collection("users").document(user.id).get().await()
            .toObject(FirestoreUser::class.java) ?: throw NotFoundException()
        }
        val phone = async { auth.getUserAsync(user.id).await().phoneNumber }

        val result = firestoreUser.await().toModelUser(phone = phone.await())

        Result.Ok(result)
    }
}
