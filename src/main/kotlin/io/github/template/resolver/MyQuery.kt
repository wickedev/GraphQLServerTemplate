package io.github.template.resolver

import com.expediagroup.graphql.generator.annotations.GraphQLIgnore
import com.expediagroup.graphql.server.operations.Query
import io.github.template.entity.User
import io.github.template.repository.UserRepository
import io.github.wickedev.coroutine.reactive.extensions.mono.await
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component

@Component
class MyQuery(
    val userRepository: UserRepository
) : Query {

    suspend fun myInfo(@GraphQLIgnore authentication: Authentication?): User? {
        return authentication?.name?.let { userRepository.findByEmail(it).await() }
    }
}
