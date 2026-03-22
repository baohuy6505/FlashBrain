import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "decks")
data class DeckEntity(
    @PrimaryKey val id: String, // UUID
    val userId: String,
    val title: String,
    val isPublic: Boolean,
    val isDeleted: Boolean,
    val createdAt: Long,
    val updatedAt: Long
)