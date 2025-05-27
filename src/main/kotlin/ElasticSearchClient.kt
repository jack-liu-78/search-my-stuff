import co.elastic.clients.elasticsearch.ElasticsearchClient
import co.elastic.clients.transport.rest_client.RestClientTransport
import co.elastic.clients.json.jackson.JacksonJsonpMapper
import org.apache.http.HttpHost
import org.elasticsearch.client.RestClient


import co.elastic.clients.elasticsearch.core.SearchRequest
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString


object ElasticsearchProvider {
    private val restClient = RestClient.builder(
        HttpHost("localhost", 9200)
    ).build()

    private val transport = RestClientTransport(restClient, JacksonJsonpMapper())

    val client = ElasticsearchClient(transport)
}

fun indexItemInElasticsearch(item: Item) {
    ElasticsearchProvider.client.index<Item> {
        it.index("items")
            .id(item.id)
            .document(item)
    }
}


fun searchItems(query: String): List<Item> {
    val response = ElasticsearchProvider.client.search(
        SearchRequest.of { s ->
            s.index("items")
                .query { q ->
                    q.multiMatch { m ->
                        m.query(query)
                            .fields("name^2", "description")
                            .fuzziness("AUTO")
                    }
                }
        },
        Map::class.java
    )

    return response.hits().hits().mapNotNull { hit ->
        val rawSource = hit.source() ?: return@mapNotNull null
        val json = Json.encodeToString(rawSource)
        try {
            Json.decodeFromString<Item>(json)
        } catch (e: Exception) {
            println("Warning: failed to decode search result: $json")
            null
        }
    }
}