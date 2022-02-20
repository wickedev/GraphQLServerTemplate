package io.github.template.resolver

import com.expediagroup.graphql.server.operations.Query
import graphql.schema.DataFetchingEnvironment
import io.github.wickedev.graphql.interfases.Node
import io.github.wickedev.graphql.repository.GraphQLNodeRepository
import io.github.wickedev.graphql.types.ID
import org.springframework.stereotype.Component
import java.util.concurrent.CompletableFuture

@Component
class NodeQuery(
    val graphQLNodeRepository: GraphQLNodeRepository,
) : Query {

    fun node(id: ID, env: DataFetchingEnvironment): CompletableFuture<Node?> =
        graphQLNodeRepository.findNodeById(id, env)
}
