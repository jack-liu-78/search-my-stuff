import kotlinx.serialization.Serializable

@Serializable
data class Item(
    val id: String,
    val name: String,
    val description: String,
    val tags: List<String>,
    val location: String,
    val createdAt: String
)


