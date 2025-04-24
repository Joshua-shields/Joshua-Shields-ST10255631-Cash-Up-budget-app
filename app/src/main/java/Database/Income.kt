import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "income_table")
data class Income(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val salary: String,

    val amount: String,

    val date: String,

    val description: String,

    val documentPath: String?, // Path to attached document
    val profileImagePath: String? // Path to profile image
)