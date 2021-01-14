package tupperdate.web

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.util.pipeline.*
import org.koin.ktor.ext.inject
import tupperdate.common.dto.MyUserDTO
import tupperdate.web.facade.profiles.Profile
import tupperdate.web.facade.profiles.ProfileFacade
import tupperdate.web.facade.profiles.toNewProfile
import tupperdate.web.facade.profiles.toUserDTO
import tupperdate.web.legacy.auth.firebaseAuthPrincipal
import tupperdate.web.model.Result
import tupperdate.web.model.map
import tupperdate.web.model.profiles.User

fun Route.endpoints() {
    route("/users") {
        val facade by this.inject<ProfileFacade>()

        put("{userId}") {
            facade.save(
                user = requireUser(),
                profileId = requireParam("userId"),
                profile = requireBody<MyUserDTO>().toNewProfile()
            ).let { respond(it) }
        }

        get("{userId}") {
            facade.read(
                user = requireUser(),
                profileId = requireParam("userId")
            ).map(Profile::toUserDTO).let { respond(it) }
        }
    }
}

private suspend inline fun <T> PipelineContext<Unit, ApplicationCall>.respond(result: Result<T>) {
    val (body, code) = when (result) {
        is Result.Ok -> result.result to HttpStatusCode.OK
        is Result.Forbidden -> result.message to HttpStatusCode.Forbidden
        is Result.BadInput -> result.message to HttpStatusCode.BadRequest
        is Result.NotFound -> result.message to HttpStatusCode.NotFound
        is Result.MissingData -> result.message to HttpStatusCode.InternalServerError
        is Result.BadServer -> result.message to HttpStatusCode.InternalServerError
    }

    call.respond(code, body ?: "Info not provided by server")
}

private suspend inline fun <reified T : Any> PipelineContext<Unit, ApplicationCall>.requireBody(): T =
    call.receive()

private fun PipelineContext<Unit, ApplicationCall>.requireParam(name: String): String =
    call.parameters[name] ?: statusException(HttpStatusCode.BadRequest)

private fun PipelineContext<Unit, ApplicationCall>.requireUser(): User =
    call.firebaseAuthPrincipal?.uid?.let(::User) ?: statusException(HttpStatusCode.Unauthorized)