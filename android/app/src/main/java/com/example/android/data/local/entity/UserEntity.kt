import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: String, // UUID từ Backend trả về
    val email: String,
    val role: String,
    val subscriptionType: String,
    val balance: Double,
    val proExpireAt: Long?,
    val createdAt: Long,
    val updatedAt: Long
)