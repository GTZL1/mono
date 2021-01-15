package tupperdate.web.facade.profiles

import tupperdate.web.model.Result
import tupperdate.web.model.map
import tupperdate.web.model.profiles.ModelNewUser
import tupperdate.web.model.profiles.User
import tupperdate.web.model.profiles.UserRepository
import tupperdate.web.model.profiles.toProfile

class ProfileFacadeImpl(
    private val users: UserRepository,
) : ProfileFacade {

    override suspend fun save(
        user: User,
        profileId: String,
        profile: NewProfile
    ): Result<Unit> {
        if (user.id.uid != profileId) return Result.Forbidden()
        val newUser = ModelNewUser(
            identifier = user.id.uid,
            displayName = profile.displayName,
            displayPicture = profile.picture,
        )

        return users.save(newUser)
    }

    override suspend fun read(
        user: User,
        profileId: String,
    ): Result<Profile> {
        if (user.id.uid != profileId) return Result.Forbidden()

        val phone = TODO()

        return users.read(user).map { it.toProfile(phone) }
    }
}
