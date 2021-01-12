package tupperdate.web.facade.profiles

data class Profile<Picture>(
    val identifier: String,
    val displayName: String,
    val picture: Picture?,
    val phone: String,
)
