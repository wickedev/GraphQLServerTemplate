package io.github.template.repository

import graphql.schema.DataFetchingEnvironment
import io.github.template.entity.Post
import io.github.wickedev.graphql.spring.data.r2dbc.repository.interfaces.GraphQLR2dbcRepository
import io.github.wickedev.graphql.types.Backward
import io.github.wickedev.graphql.types.Connection
import io.github.wickedev.graphql.types.ID
import org.springframework.stereotype.Repository
import java.util.concurrent.CompletableFuture

@Repository
interface PostRepository : GraphQLR2dbcRepository<Post> {
    fun connectionByAuthorId(id: ID, backward: Backward, env: DataFetchingEnvironment): CompletableFuture<Connection<Post>>
}
