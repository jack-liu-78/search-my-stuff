import io.ktor.server.routing.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.http.*
import java.util.UUID
import kotlinx.datetime.Clock
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.TimeZone


fun Route.itemRoutes() {
    post("/items") {
        val newItem = call.receive<Item>()

        // Ensure ID + createdAt are set
        val itemWithId = newItem.copy(
            id = newItem.id.ifBlank { UUID.randomUUID().toString() },
            createdAt = newItem.createdAt.ifBlank {
                Clock.System.now().toLocalDateTime(TimeZone.UTC).toString()
            }
        )

        insertItem(itemWithId)
        println("Indexing item to Elasticsearch: ${itemWithId.id}")
        indexItemInElasticsearch(itemWithId)
        println("Elasticsearch indexing attempted")
        call.respond(HttpStatusCode.Created, itemWithId)
    }
    get("/items") {
        val items = getAllItems()
        call.respond(items)
    }

    get("/items/{id}") {
        val id = call.parameters["id"]
        val item = id?.let { getItemById(it) }

        if (item != null) {
            call.respond(item)
        } else {
            call.respond(HttpStatusCode.NotFound, "Item not found")
        }
    }

    get("/search") {
        val query = call.request.queryParameters["q"]

        if (query.isNullOrBlank()) {
            call.respond(HttpStatusCode.BadRequest, "Missing search query param `q`")
            return@get
        }

        val results = searchItems(query)
        call.respond(results)
    }
}