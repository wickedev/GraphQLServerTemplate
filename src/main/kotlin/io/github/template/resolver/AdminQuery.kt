package io.github.template.resolver

import com.expediagroup.graphql.server.operations.Query
import graphql.schema.DataFetchingEnvironment
import io.github.template.repository.UserRepository
import io.github.template.values.UserConnect
import io.github.template.values.UserEdge
import io.github.wickedev.graphql.Auth
import io.github.wickedev.graphql.types.Backward
import io.github.wickedev.graphql.types.ID
import org.springframework.stereotype.Component
import java.util.concurrent.CompletableFuture

@Component
class AdminQuery(private val userRepository: UserRepository) : Query {
    @Auth("hasRole('ADMIN')")
    fun users(last: Int?, before: ID?, env: DataFetchingEnvironment): CompletableFuture<UserConnect> {
        return userRepository.connection(Backward(last, before), env)
            .thenApply { UserConnect(it.edges.map { e -> UserEdge(e.node, e.cursor) }, it.pageInfo) }
    }
}
