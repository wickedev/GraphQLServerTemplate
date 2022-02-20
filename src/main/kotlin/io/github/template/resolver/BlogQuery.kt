package io.github.template.resolver

import com.expediagroup.graphql.server.operations.Query
import graphql.schema.DataFetchingEnvironment
import io.github.template.entity.Post
import io.github.template.repository.PostRepository
import io.github.template.values.PostConnect
import io.github.template.values.PostEdge
import io.github.wickedev.graphql.types.Forward
import io.github.wickedev.graphql.types.ID
import io.github.wickedev.graphql.types.Order
import org.springframework.data.relational.core.query.Criteria
import org.springframework.stereotype.Component
import java.util.concurrent.CompletableFuture

@Component
class BlogQuery(
    val postRepository: PostRepository,
) : Query {

    fun posts(
        first: Int?,
        after: ID?,
        orderBy: List<Order>?,
        search: String? = null,
        env: DataFetchingEnvironment
    ): CompletableFuture<PostConnect> {
        val criteria = Criteria.from(
            listOfNotNull(
                search?.let { Criteria.where("title").like("%$it%") },
                search?.let { Criteria.where("content").like("%$it%") }
            )
        )

        return postRepository.connection(Forward(first, after, orderBy), criteria, env)
            .thenApply {
                PostConnect(
                    it.edges.map { e -> PostEdge(e.node, e.cursor) },
                    it.pageInfo
                )
            }
    }

    fun post(id: ID, env: DataFetchingEnvironment): CompletableFuture<Post?> {
        return postRepository.findById(id, env)
    }
}
