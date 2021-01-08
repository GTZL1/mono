package tupperdate.android.data.features.auth.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import tupperdate.android.data.InternalDataApi

@Entity(tableName = "profiles")
@InternalDataApi
data class ProfileEntity(
    @PrimaryKey
    @ColumnInfo(name = "uid") val uid: String,
    @ColumnInfo(name = "name") val displayName: String,
    @ColumnInfo(name = "picture") val profilePicture: String?,
    @ColumnInfo(name = "phone") val phone: String,
    @ColumnInfo(name = "token") val token: String,
    @ColumnInfo(name = "token_expiry") val tokenExpirySeconds: Long,
)
