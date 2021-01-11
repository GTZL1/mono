package tupperdate.web

import com.google.firebase.cloud.FirestoreClient
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlinx.coroutines.runBlocking
import tupperdate.web.util.*
import kotlin.test.*


// Initialise FirebaseApp
val firebase = initialiseApp()
val firestore = FirestoreClient.getFirestore(firebase)

class SimpleTest {
    @Test
    fun testUnauthorized() {
        withTestApplication {
            application.installServer(firebase)

            with(handleRequest(HttpMethod.Get, "/recipes")) {
                assertEquals(HttpStatusCode.Unauthorized, response.status())
            }
        }
    }

    @Test
    fun testEmulator() {
        val doc = firestore.collection("emulatorOnlyDeleteMe").document("uniqueId")
        runBlocking {
            doc.set(mapOf("displayName" to "Emulatron3000")).await()
            val name = doc.get().await().get("displayName") as String
            assertEquals(name, "Emulatron3000")
        }
    }
}