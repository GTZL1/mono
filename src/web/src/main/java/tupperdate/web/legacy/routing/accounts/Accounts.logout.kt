package tupperdate.web.legacy.routing.accounts

import com.google.firebase.auth.FirebaseAuth
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import tupperdate.web.legacy.auth.firebaseAuthPrincipal
import tupperdate.web.legacy.exceptions.statusException

fun Route.logout(auth: FirebaseAuth) = post("logout") {
    val uid = call.firebaseAuthPrincipal?.uid ?: statusException(HttpStatusCode.Unauthorized)
    // TODO: This revokes all refresh tokens from every logged in device. Maybe do something else
    auth.revokeRefreshTokens(uid)

    call.respond(HttpStatusCode.OK)
}
