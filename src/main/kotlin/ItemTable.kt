import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction


object Items : Table("items") {
    val id = varchar("id", 36) // UUID as string
    val name = varchar("name", 255)
    val description = text("description")
    val tags = text("tags") // comma-separated string
    val location = varchar("location", 255) // New field
    val createdAt = varchar("created_at", 30)

    override val primaryKey = PrimaryKey(id)
}


fun ResultRow.toItem(): Item {
    return Item(
        id = this[Items.id],
        name = this[Items.name],
        description = this[Items.description],
        tags = this[Items.tags].split(",").map { it.trim() },
        location = this[Items.location],
        createdAt = this[Items.createdAt]
    )
}

fun insertItem(item: Item) {
    transaction {
        Items.insert {
            it[id] = item.id
            it[name] = item.name
            it[description] = item.description
            it[tags] = item.tags.joinToString(",")
            it[location] = item.location
            it[createdAt] = item.createdAt
        }
    }
}

fun getAllItems(): List<Item> {
    return transaction {
        Items.selectAll().map { it.toItem() }
    }
}

fun getItemById(id: String): Item? {
    return transaction {
        Items.select { Items.id eq id }
            .map { it.toItem() }
            .singleOrNull()
    }
}

